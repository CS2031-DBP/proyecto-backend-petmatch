package org.example.petmatch.Volunteer_Program.Domain;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Shelter.Domain.Shelter;
import org.example.petmatch.Shelter.Exceptions.ShelterNotFoundException;
import org.example.petmatch.Shelter.Infraestructure.ShelterRepository;
import org.example.petmatch.Common.NewIdDTO;
import org.example.petmatch.Inscription.domain.Inscription;
import org.example.petmatch.Inscription.exception.AlreadyEnrolledException;
import org.example.petmatch.Inscription.exception.InscriptionNotFoundException;
import org.example.petmatch.Inscription.infrastructure.InscriptionRepository;
import org.example.petmatch.User.Domain.User;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramRequestDto;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramResponseDto;
import org.example.petmatch.Volunteer_Program.Infraestructure.VolunteerProgramRepository;
import org.example.petmatch.Volunteer_Program.exception.VolunteerProgramIsFullException;
import org.example.petmatch.Volunteer_Program.exception.VolunteerProgramNotFoundException;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.example.petmatch.Volunteer.Domain.Volunteer;
import org.example.petmatch.Volunteer.Infraestructure.VolunteerRepository;
import org.example.petmatch.Volunteer.dto.VolunteerResponseDto;
import org.example.petmatch.Volunteer.exception.VolunteerNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerProgramService {
    private final VolunteerProgramRepository programaRepository;
    private final ModelMapper modelMapper;
    private final ShelterRepository shelterRepository;
    private final VolunteerRepository volunteerRepository;
    private final InscriptionRepository inscriptionRepository;

    public List<VolunteerProgramResponseDto> getAllProgramas() {
        List<VolunteerProgram> programas = programaRepository.findAll();
        return programas.stream()
                .map(programa -> modelMapper.map(programa, VolunteerProgramResponseDto.class))
                .toList();
    }

    public List<VolunteerResponseDto> getAllVoluntariosInPrograma(Long id) {
        VolunteerProgram programa = programaRepository.findById(id)
                .orElseThrow(() -> new VolunteerProgramNotFoundException("Programa con id " + id + " no encontrado"));

        List<Volunteer> volunteers = programa.getVoluntarios();
        return volunteers.stream()
                .map(voluntario -> modelMapper.map(voluntario, VolunteerResponseDto.class))
                .toList();
    }

    @Transactional
    public NewIdDTO createPrograma(String email, VolunteerProgramRequestDto programaDto) {
        Shelter shelter = shelterRepository.findByEmail(email)
                .orElseThrow(() -> new ShelterNotFoundException("Albergue con email " + email + " no encontrado"));

        VolunteerProgram programa = modelMapper.map(programaDto, VolunteerProgram.class);
        programa.setShelter(shelter);
        programa.setLocation(shelter.getAddress());
        programa.setStatus(VolunteerProgramStatus.ABIERTO);
        programa.setEnrolledVolunteers(0);

        VolunteerProgram savedPrograma = programaRepository.save(programa);
        return new NewIdDTO(savedPrograma.getId());
    }

    // ✅ MÉTODO CORREGIDO - Inscribir voluntario a programa
    @Transactional
    public void enrollingVolunteerPrograma(Long programaId, String email) {
        // 1️⃣ Buscar el programa
        VolunteerProgram programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new VolunteerProgramNotFoundException("Programa con id " + programaId + " no encontrado"));

        // 2️⃣ Buscar el voluntario (todos los usuarios son voluntarios automáticamente)
        Volunteer volunteer = volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new VolunteerNotFoundException("Voluntario con email " + email + " no encontrado"));

        // 3️⃣ Verificar si ya está inscrito
        if(inscriptionRepository.existsByVolunteerIdAndVolunteerProgramId(volunteer.getId(), programaId)) {
            throw new AlreadyEnrolledException("Ya estás inscrito en este programa");
        }

        // 4️⃣ Verificar si el programa está lleno
        if(programa.isLleno()){
            throw new VolunteerProgramIsFullException("El programa está lleno");
        }

        // 5️⃣ Crear la inscripción (SOLO UNA VEZ)
        Inscription inscription = new Inscription(volunteer, programa);
        inscriptionRepository.save(inscription);

        // 6️⃣ Actualizar el contador de voluntarios inscritos
        programa.setEnrolledVolunteers(programa.getEnrolledVolunteers() + 1);

        // 7️⃣ Cambiar el estado si se alcanzó el límite
        if(programa.getEnrolledVolunteers() >= programa.getNecessaryVolunteers()){
            programa.setStatus(VolunteerProgramStatus.LLENO);
        }

        // 8️⃣ Guardar el programa con los cambios
        programaRepository.save(programa);
    }

    // ✅ MÉTODO CORREGIDO - Albergue elimina un voluntario del programa
    @Transactional
    public void unsubscribeVolunteerProgramOnShelter(Long programaId, Long voluntarioId, String albergueEmail){
        // 1️⃣ Buscar el programa
        VolunteerProgram programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new VolunteerProgramNotFoundException("Programa con id " + programaId + " no encontrado"));

        // 2️⃣ Verificar que el albergue sea el dueño del programa
        Shelter shelter = shelterRepository.findByEmail(albergueEmail)
                .orElseThrow(() -> new ShelterNotFoundException("Albergue con email " + albergueEmail + " no encontrado"));

        if(!programa.getShelter().getId().equals(shelter.getId())){
            throw new RuntimeException("No tienes permiso para modificar este programa");
        }

        // 3️⃣ Verificar que el voluntario existe
        Volunteer volunteer = volunteerRepository.findById(voluntarioId)
                .orElseThrow(() -> new VolunteerNotFoundException("Voluntario con id " + voluntarioId + " no encontrado"));

        // 4️⃣ Buscar la inscripción
        Inscription inscription = inscriptionRepository.findByVolunteerIdAndVolunteerProgramId(voluntarioId, programaId)
                .orElseThrow(() -> new InscriptionNotFoundException("El voluntario no está inscrito en este programa"));

        // 5️⃣ Eliminar la inscripción
        inscriptionRepository.delete(inscription);

        // 6️⃣ Actualizar el contador (sin bajar de 0)
        programa.setEnrolledVolunteers(Math.max(0, programa.getEnrolledVolunteers() - 1));

        // 7️⃣ Si estaba lleno y ahora hay espacio, cambiar estado a ABIERTO
        if(programa.getStatus() == VolunteerProgramStatus.LLENO &&
                programa.getEnrolledVolunteers() < programa.getNecessaryVolunteers()){
            programa.setStatus(VolunteerProgramStatus.ABIERTO);
        }

        // 8️⃣ Guardar cambios
        programaRepository.save(programa);
    }

    // ✅ MÉTODO CORREGIDO - Voluntario se desinscribe del programa
    @Transactional
    public void desinscribirDePrograma(Long programaId, String email) {
        // 1️⃣ Buscar el programa
        VolunteerProgram programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new VolunteerProgramNotFoundException("Programa con id " + programaId + " no encontrado"));

        // 2️⃣ Buscar el voluntario
        Volunteer volunteer = volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new VolunteerNotFoundException("Voluntario con email " + email + " no encontrado"));

        // 3️⃣ Buscar la inscripción
        Inscription inscription = inscriptionRepository.findByVolunteerIdAndVolunteerProgramId(volunteer.getId(), programaId)
                .orElseThrow(() -> new InscriptionNotFoundException("No estás inscrito en este programa"));

        // 4️⃣ Eliminar la inscripción
        inscriptionRepository.delete(inscription);

        // 5️⃣ Actualizar el contador
        programa.setEnrolledVolunteers(Math.max(0, programa.getEnrolledVolunteers() - 1));

        // 6️⃣ Si estaba lleno y ahora hay espacio, cambiar estado
        if(programa.getStatus() == VolunteerProgramStatus.LLENO &&
                programa.getEnrolledVolunteers() < programa.getNecessaryVolunteers()){
            programa.setStatus(VolunteerProgramStatus.ABIERTO);
        }

        // 7️⃣ Guardar cambios
        programaRepository.save(programa);
    }

    // ✅ MÉTODO CORREGIDO - Albergue elimina su programa
    @Transactional
    public void albergueDeletePrograma(Long id, String albergueEmail) {
        // 1️⃣ Buscar el programa
        VolunteerProgram programa = programaRepository.findById(id)
                .orElseThrow(() -> new VolunteerProgramNotFoundException("Programa con id " + id + " no encontrado"));

        // 2️⃣ Verificar que el albergue sea el dueño
        Shelter shelter = shelterRepository.findByEmail(albergueEmail)
                .orElseThrow(() -> new ShelterNotFoundException("Albergue con email " + albergueEmail + " no encontrado"));

        if(!programa.getShelter().getId().equals(shelter.getId())){
            throw new RuntimeException("No tienes permiso para eliminar este programa");
        }

        // 3️⃣ Eliminar el programa (las inscripciones se eliminan automáticamente por orphanRemoval)
        programaRepository.deleteById(id);
    }
}