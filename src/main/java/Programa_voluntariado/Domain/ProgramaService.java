package Programa_voluntariado.Domain;

import Albergue.Domain.Albergue;
import Albergue.Infraestructure.AlbergueRepository;
import Common.NewIdDTO;
import Programa_voluntariado.DTO.ProgramaRequestDto;
import Programa_voluntariado.DTO.ProgramaResponseDto;
import Programa_voluntariado.Infraestructure.ProgramaVoluntariadoRepositorio;
import Programa_voluntariado.exception.ProgramaIsFullException;
import Programa_voluntariado.exception.ProgramaNotFoundException;
import User.Infraestructure.UserRepository;
import User.dto.UserResponseDto;
import Voluntario.Domain.Voluntario;
import Voluntario.Infraestructure.VoluntarioRepository;
import Voluntario.exception.VoluntarioNotFoundException;
import VoluntarioPrograma.domain.Inscripcion;
import VoluntarioPrograma.exception.AlreadyEnrolledException;
import VoluntarioPrograma.infrastructure.InscripcionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramaService {
    private final ProgramaVoluntariadoRepositorio programaVoluntariadoRepositorio;
    private final ModelMapper modelMapper;
    private final AlbergueRepository albergueRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final UserRepository userRepository;
    private final InscripcionRepository inscripcionRepository;

    public List<ProgramaResponseDto> getAllProgramas() {
        List<ProgramaVoluntariado> programas = programaVoluntariadoRepositorio.findAll();
        List<ProgramaResponseDto> programasDtos = programas.stream()
                .map(programa -> modelMapper.map(programa, ProgramaResponseDto.class))
                .toList();
        return programasDtos;
    }

    public List<UserResponseDto> getAllVoluntariosInPrograma(Long id) {
        ProgramaVoluntariado programa = programaVoluntariadoRepositorio.findById(id).orElseThrow(() -> new ProgramaNotFoundException("Programa de voluntariado con id" + id + " no encontrado"));
        List<Voluntario> voluntarios = programa.getVoluntarios();
        List<UserResponseDto> usuarios = voluntarios.stream()
                .map(voluntario -> modelMapper.map(voluntario, UserResponseDto.class))
                .toList();
        return usuarios;
    }

    public NewIdDTO createPrograma(Long AlbergueId, ProgramaRequestDto programaResponseDto) {
        ProgramaVoluntariado programa = modelMapper.map(programaResponseDto, ProgramaVoluntariado.class);
        Albergue albergue = albergueRepository.findById(AlbergueId).orElseThrow(() -> new RuntimeException("Albergue con id " + AlbergueId + " no encontrado"));
        programa.setAlbergue(albergue);
        programa.setUbicacion(albergue.getAddress());
        ProgramaVoluntariado savedPrograma = programaVoluntariadoRepositorio.save(programa);
        return new NewIdDTO(savedPrograma.getId());
    }

    @Transactional
    public void inscribirVoluntarioAPrograma(Long programaId, Long voluntarioId) {

        ProgramaVoluntariado programa = programaVoluntariadoRepositorio.findById(programaId)
                .orElseThrow(() -> new ProgramaNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));
        if(!inscripcionRepository.existsByVoluntarioIdAndProgramaVoluntariadoId(voluntarioId, programaId)) {
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

    public void desinscribirVoluntarioDePrograma(Long programaId, Long voluntarioId) {
        ProgramaVoluntariado programa = programaVoluntariadoRepositorio.findById(programaId)
                .orElseThrow(() -> new ProgramaNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));
        Voluntario voluntario = voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new VoluntarioNotFoundException("Voluntario con id " + voluntarioId + " no encontrado"));
        Inscripcion inscripcion = inscripcionRepository.findByVoluntarioIdAndProgramaVoluntariadoId(voluntarioId, programaId)
                .orElseThrow(() -> new RuntimeException("El voluntario con id " + voluntarioId + " no está inscrito en el programa con id " + programaId));
        voluntario.removeInscripcion(programa);
        inscripcionRepository.delete(inscripcion);
    }

    public void deletePrograma(Long id) {
        if (!programaVoluntariadoRepositorio.existsById(id)) {
            throw new ProgramaNotFoundException("Programa de voluntariado con id " + id + " no encontrado");
        }
        programaVoluntariadoRepositorio.deleteById(id);
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
}
