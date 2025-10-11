package Programa_voluntariado.Domain;

import Programa_voluntariado.DTO.ProgramaResponseDto;
import Programa_voluntariado.Infraestructure.ProgramaVoluntariadoRepositorio;
import User.dto.UserResponseDto;
import Voluntario.Domain.Voluntario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramaService {
    private final ProgramaVoluntariadoRepositorio programaVoluntariadoRepositorio;
    private final ModelMapper modelMapper;

    public List<ProgramaResponseDto> getAllProgramas() {
        List<ProgramaVoluntariado> programas = programaVoluntariadoRepositorio.findAll();
        List<ProgramaResponseDto> programasDtos = programas.stream()
                .map(programa -> modelMapper.map(programa, ProgramaResponseDto.class))
                .toList();
        return programasDtos;
    }

    public List<UserResponseDto> getAllVoluntariosInPrograma(Long id) {
        ProgramaVoluntariado programa = programaVoluntariadoRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Programa no encontrado"));
        List<Voluntario> voluntarios = programa.getVoluntarios();
        List<UserResponseDto> usuarios = voluntarios.stream()
                .map(voluntario -> modelMapper.map(voluntario.getUsuario(), UserResponseDto.class))
                .toList();
        return usuarios;
    }
}
