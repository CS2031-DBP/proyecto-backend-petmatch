package org.example.petmatch.Voluntario.Domain;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Inscripcion.infrastructure.InscripcionRepository;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaResponseDto;
import org.example.petmatch.Voluntario.Infraestructure.VoluntarioRepository;
import org.example.petmatch.Voluntario.dto.VoluntarioResponseDto;
import org.example.petmatch.Voluntario.exception.VoluntarioNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoluntarioService {
    private final VoluntarioRepository voluntarioRepository;
    private final InscripcionRepository inscripcionRepository;
    private final ModelMapper modelMapper;

    public List<VoluntarioResponseDto> getAllVoluntarios(){
        List<Voluntario> voluntarios = voluntarioRepository.findAll();
        List<VoluntarioResponseDto> voluntariosDtos = voluntarios.stream()
                .map(voluntario -> modelMapper.map(voluntario, VoluntarioResponseDto.class))
                .toList();
        return voluntariosDtos;
    }

    public List<ProgramaResponseDto> getProgramaByVoluntarioId(Long id){
        Voluntario voluntario = voluntarioRepository.findById(id).orElseThrow(() -> new VoluntarioNotFoundException("Voluntario no encontrado con id: " + id));
        return voluntario.getProgramas().stream().map(programa -> modelMapper.map(programa, ProgramaResponseDto.class)).toList();
    }
}
