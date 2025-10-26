package org.example.petmatch.Programa_voluntariado.Domain;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Albergue.Domain.Albergue;
import org.example.petmatch.Albergue.Exceptions.AlbergueNotFoundException;
import org.example.petmatch.Albergue.Infraestructure.AlbergueRepository;
import org.example.petmatch.Common.NewIdDTO;
import org.example.petmatch.Inscripcion.domain.Inscripcion;
import org.example.petmatch.Inscripcion.exception.AlreadyEnrolledException;
import org.example.petmatch.Inscripcion.exception.InscripcionNotFoundException;
import org.example.petmatch.Inscripcion.infrastructure.InscripcionRepository;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaRequestDto;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaResponseDto;
import org.example.petmatch.Programa_voluntariado.Infraestructure.ProgramaVoluntariadoRepository;
import org.example.petmatch.Programa_voluntariado.exception.ProgramaIsFullException;
import org.example.petmatch.Programa_voluntariado.exception.ProgramaNotFoundException;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.example.petmatch.Voluntario.Domain.Voluntario;
import org.example.petmatch.Voluntario.Infraestructure.VoluntarioRepository;
import org.example.petmatch.Voluntario.dto.VoluntarioResponseDto;
import org.example.petmatch.Voluntario.exception.VoluntarioNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramaService {
    private final ProgramaVoluntariadoRepository programaRepository;
    private final ModelMapper modelMapper;
    private final AlbergueRepository albergueRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final UserRepository userRepository;
    private final InscripcionRepository inscripcionRepository;

    public List<ProgramaResponseDto> getAllProgramas() {
        List<ProgramaVoluntariado> programas = programaRepository.findAll();
        List<ProgramaResponseDto> programasDtos = programas.stream()
                .map(programa -> modelMapper.map(programa, ProgramaResponseDto.class))
                .toList();
        return programasDtos;
    }

    public List<VoluntarioResponseDto> getAllVoluntariosInPrograma(Long id) {
        ProgramaVoluntariado programa = programaRepository.findById(id).orElseThrow(() -> new ProgramaNotFoundException("Programa de voluntariado con id" + id + " no encontrado"));
        List<Voluntario> voluntarios = programa.getVoluntarios();
        List<VoluntarioResponseDto> usuarios = voluntarios.stream()
                .map(voluntario -> modelMapper.map(voluntario, VoluntarioResponseDto.class))
                .toList();
        return usuarios;
    }

    public NewIdDTO createPrograma(String email, ProgramaRequestDto programaResponseDto) {
        Albergue albergueUser = albergueRepository.findByEmail(email).orElseThrow(() -> new AlbergueNotFoundException("Albergue con email " + email + " no encontrado"));
        Long AlbergueId = albergueUser.getId();
        ProgramaVoluntariado programa = modelMapper.map(programaResponseDto, ProgramaVoluntariado.class);
        Albergue albergue = albergueRepository.findById(AlbergueId).orElseThrow(() -> new RuntimeException("Albergue con id " + AlbergueId + " no encontrado"));
        programa.setAlbergue(albergue);
        programa.setUbicacion(albergue.getAddress());
        ProgramaVoluntariado savedPrograma = programaRepository.save(programa);
        return new NewIdDTO(savedPrograma.getId());
    }

    @Transactional
    public void inscribirVoluntarioAPrograma(Long programaId, Long voluntarioId) {

        ProgramaVoluntariado programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new ProgramaNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));
        if(inscripcionRepository.existsByVoluntarioIdAndProgramaVoluntariadoId(voluntarioId, programaId)) {
            throw new AlreadyEnrolledException("El voluntario con id " + voluntarioId + " ya está inscrito en el programa con id " + programaId);
        }
        if(programa.isLleno()){
            throw new ProgramaIsFullException("El programa con id " + programaId + " ya ha alcanzado el número máximo de voluntarios");
        }

        Integer numInscritos = programa.getNumeroVoluntariosInscritos();
        programa.setNumeroVoluntariosInscritos(numInscritos + 1);
        if(numInscritos + 1 >= programa.getNumeroVoluntariosNecesarios()){
            programa.setStatus(ProgramaStatus.LLENO);
        }
        Voluntario voluntario = encontrarOCrearVoluntario(voluntarioId);
        voluntario.addInscripcion(programa);
        Inscripcion inscripcion = new Inscripcion(voluntario, programa);
        inscripcionRepository.save(inscripcion);
    }

    @Transactional
    public void desinscribirVoluntarioDePrograma(Long programaId, Long voluntarioId) {
        ProgramaVoluntariado programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new ProgramaNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));
        Voluntario voluntario = voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new VoluntarioNotFoundException("Voluntario con id " + voluntarioId + " no encontrado"));
        Inscripcion inscripcion = inscripcionRepository.findByVoluntarioIdAndProgramaVoluntariadoId(voluntarioId, programaId)
                .orElseThrow(() -> new RuntimeException("El voluntario con id " + voluntarioId + " no está inscrito en el programa con id " + programaId));
        voluntario.removeInscripcion(programa);
        inscripcionRepository.delete(inscripcion);
    }

    @Transactional
    public void desinscribirVoluntarioDeProgramaAlbergue(Long programaId, Long voluntarioId, String albergueEmail){
        ProgramaVoluntariado programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new ProgramaNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));

        Albergue albergue = albergueRepository.findByEmail(albergueEmail)
                .orElseThrow(() -> new AlbergueNotFoundException("Albergue con email " + albergueEmail + " no encontrado"));

        if(!programa.getAlbergue().getId().equals(albergue.getId())){
            throw new RuntimeException("El albergue con email " + albergueEmail + " no es el creador del programa con id " + programaId);
        }

        Voluntario voluntario = voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new VoluntarioNotFoundException("Voluntario con id " + voluntarioId + " no encontrado"));

        Inscripcion inscripcion = inscripcionRepository.findByVoluntarioIdAndProgramaVoluntariadoId(voluntarioId, programaId)
                .orElseThrow(() -> new InscripcionNotFoundException("El voluntario con id " + voluntarioId + " no está inscrito en el programa con id " + programaId));

        voluntario.removeInscripcion(programa);
        inscripcionRepository.delete(inscripcion);
    }

    @Transactional
    public void desinscribirDePrograma(Long programaId, String username) {
        ProgramaVoluntariado programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new ProgramaNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));

        Voluntario voluntario = voluntarioRepository.findByEmail(username)
                .orElseThrow(() -> new VoluntarioNotFoundException("Voluntario con email " + username + " no encontrado"));

        if(inscripcionRepository.findByVoluntarioIdAndProgramaVoluntariadoId(voluntario.getId(), programaId).isEmpty()){
            throw new InscripcionNotFoundException("El voluntario con id " + voluntario.getId() + " no está inscrito en el programa con id " + programaId);
        }

        voluntario.removeInscripcion(programa);
        voluntarioRepository.save(voluntario);
    }

    public void deletePrograma(Long id) {
        if (!programaRepository.existsById(id)) {
            throw new ProgramaNotFoundException("Programa de voluntariado con id " + id + " no encontrado");
        }
        programaRepository.deleteById(id);
    }

    public Voluntario encontrarOCrearVoluntario(Long usuarioId) {
        return voluntarioRepository.findById(usuarioId).orElseGet(() -> {
            if (!userRepository.existsById(usuarioId)) {
                throw new RuntimeException("Usuario con id " + usuarioId + " no encontrado");
            }
            Voluntario nuevoVoluntario = new Voluntario();
            nuevoVoluntario.setId(usuarioId);
            return voluntarioRepository.save(nuevoVoluntario);
        });

    }

    public void albergueDeletePrograma(Long id, String albergueEmail) {
        ProgramaVoluntariado programa = programaRepository.findById(id)
                .orElseThrow(() -> new ProgramaNotFoundException("Programa de voluntariado con id " + id + " no encontrado"));

        Albergue albergue = albergueRepository.findByEmail(albergueEmail)
                .orElseThrow(() -> new AlbergueNotFoundException("Albergue con email " + albergueEmail + " no encontrado"));

        if(!programa.getAlbergue().getId().equals(albergue.getId())){
            throw new RuntimeException("El albergue con email " + albergueEmail + " no es el creador del programa con id " + id);
        }

        programaRepository.deleteById(id);
    }
}
