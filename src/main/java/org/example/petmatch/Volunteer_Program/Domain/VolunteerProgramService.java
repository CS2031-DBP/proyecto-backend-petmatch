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
    private final UserRepository userRepository;
    private final InscriptionRepository inscriptionRepository;

    public List<VolunteerProgramResponseDto> getAllProgramas() {
        List<VolunteerProgram> programas = programaRepository.findAll();
        List<VolunteerProgramResponseDto> programasDtos = programas.stream()
                .map(programa -> modelMapper.map(programa, VolunteerProgramResponseDto.class))
                .toList();
        return programasDtos;
    }

    public List<VolunteerResponseDto> getAllVoluntariosInPrograma(Long id) {
        VolunteerProgram programa = programaRepository.findById(id).orElseThrow(() -> new VolunteerProgramNotFoundException("Programa de voluntariado con id" + id + " no encontrado"));
        List<Volunteer> volunteers = programa.getVoluntarios();
        List<VolunteerResponseDto> usuarios = volunteers.stream()
                .map(voluntario -> modelMapper.map(voluntario, VolunteerResponseDto.class))
                .toList();
        return usuarios;
    }

    public NewIdDTO createPrograma(String email, VolunteerProgramRequestDto programaResponseDto) {
        Shelter shelterUser = shelterRepository.findByEmail(email).orElseThrow(() -> new ShelterNotFoundException("Albergue con email " + email + " no encontrado"));
        Long AlbergueId = shelterUser.getId();
        VolunteerProgram programa = modelMapper.map(programaResponseDto, VolunteerProgram.class);
        Shelter shelter = shelterRepository.findById(AlbergueId).orElseThrow(() -> new RuntimeException("Albergue con id " + AlbergueId + " no encontrado"));
        programa.setShelter(shelter);
        programa.setLocation(shelter.getAddress());
        VolunteerProgram savedPrograma = programaRepository.save(programa);
        return new NewIdDTO(savedPrograma.getId());
    }

    @Transactional
    public void enrollingVolunteerPrograma(Long programaId, Long voluntarioId) {

        VolunteerProgram programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new VolunteerProgramNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));
        if(inscriptionRepository.existsByVolunteerIdAndVolunteerProgramId(voluntarioId, programaId)) {
            throw new AlreadyEnrolledException("El voluntario con id " + voluntarioId + " ya está inscrito en el programa con id " + programaId);
        }
        if(programa.isLleno()){
            throw new VolunteerProgramIsFullException("El programa con id " + programaId + " ya ha alcanzado el número máximo de voluntarios");
        }

        Integer numInscritos = programa.getEnrolledVolunteers();
        programa.setEnrolledVolunteers(numInscritos + 1);
        if(numInscritos + 1 >= programa.getNecessaryVolunteers()){
            programa.setStatus(VolunteerProgramStatus.LLENO);
        }
        Volunteer volunteer = encontrarOCrearVoluntario(voluntarioId);
        volunteer.addInscription(programa);
        Inscription inscription = new Inscription(volunteer, programa);
        inscriptionRepository.save(inscription);
    }

    @Transactional
    public void unsubscribeVolunteerProgram(Long programaId, Long voluntarioId) {
        VolunteerProgram programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new VolunteerProgramNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));
        Volunteer volunteer = volunteerRepository.findById(voluntarioId)
                .orElseThrow(() -> new VolunteerNotFoundException("Voluntario con id " + voluntarioId + " no encontrado"));
        Inscription inscription = inscriptionRepository.findByVolunteerIdAndVolunteerProgramId(voluntarioId, programaId)
                .orElseThrow(() -> new RuntimeException("El voluntario con id " + voluntarioId + " no está inscrito en el programa con id " + programaId));
        volunteer.removeInscription(programa);
        inscriptionRepository.delete(inscription);
    }

    @Transactional
    public void unsubscribeVolunteerProgramOnShelter(Long programaId, Long voluntarioId, String albergueEmail){
        VolunteerProgram programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new VolunteerProgramNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));

        Shelter shelter = shelterRepository.findByEmail(albergueEmail)
                .orElseThrow(() -> new ShelterNotFoundException("Albergue con email " + albergueEmail + " no encontrado"));

        if(!programa.getShelter().getId().equals(shelter.getId())){
            throw new RuntimeException("El albergue con email " + albergueEmail + " no es el creador del programa con id " + programaId);
        }

        Volunteer volunteer = volunteerRepository.findById(voluntarioId)
                .orElseThrow(() -> new VolunteerNotFoundException("Voluntario con id " + voluntarioId + " no encontrado"));

        Inscription inscription = inscriptionRepository.findByVolunteerIdAndVolunteerProgramId(voluntarioId, programaId)
                .orElseThrow(() -> new InscriptionNotFoundException("El voluntario con id " + voluntarioId + " no está inscrito en el programa con id " + programaId));

        volunteer.removeInscription(programa);
        inscriptionRepository.delete(inscription);
    }

    @Transactional
    public void desinscribirDePrograma(Long programaId, String username) {
        VolunteerProgram programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new VolunteerProgramNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));

        Volunteer volunteer = volunteerRepository.findByEmail(username)
                .orElseThrow(() -> new VolunteerNotFoundException("Voluntario con email " + username + " no encontrado"));

        if(inscriptionRepository.findByVolunteerIdAndVolunteerProgramId(volunteer.getId(), programaId).isEmpty()){
            throw new InscriptionNotFoundException("El voluntario con id " + volunteer.getId() + " no está inscrito en el programa con id " + programaId);
        }

        volunteer.removeInscription(programa);
        volunteerRepository.save(volunteer);
    }

    public void deletePrograma(Long id) {
        if (!programaRepository.existsById(id)) {
            throw new VolunteerProgramNotFoundException("Programa de voluntariado con id " + id + " no encontrado");
        }
        programaRepository.deleteById(id);
    }

    public Volunteer encontrarOCrearVoluntario(Long usuarioId) {
        return volunteerRepository.findById(usuarioId).orElseGet(() -> {
            if (!userRepository.existsById(usuarioId)) {
                throw new RuntimeException("Usuario con id " + usuarioId + " no encontrado");
            }
            Volunteer nuevoVolunteer = new Volunteer();
            nuevoVolunteer.setId(usuarioId);
            return volunteerRepository.save(nuevoVolunteer);
        });

    }

    public void albergueDeletePrograma(Long id, String albergueEmail) {
        VolunteerProgram programa = programaRepository.findById(id)
                .orElseThrow(() -> new VolunteerProgramNotFoundException("Programa de voluntariado con id " + id + " no encontrado"));

        Shelter shelter = shelterRepository.findByEmail(albergueEmail)
                .orElseThrow(() -> new ShelterNotFoundException("Albergue con email " + albergueEmail + " no encontrado"));

        if(!programa.getShelter().getId().equals(shelter.getId())){
            throw new RuntimeException("El albergue con email " + albergueEmail + " no es el creador del programa con id " + id);
        }

        programaRepository.deleteById(id);
    }
}
