package org.example.petmatch.Programa_voluntariado.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petmatch.Common.NewIdDTO;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaRequestDto;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaResponseDto;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaService;
import org.example.petmatch.Voluntario.dto.VoluntarioResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programas")
@RequiredArgsConstructor
public class ProgramaVoluntariadoController {
    private final ProgramaService programaService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ProgramaResponseDto>> getAllProgramas() {
        List<ProgramaResponseDto> programas = programaService.getAllProgramas().stream().map(programa -> modelMapper.map(programa, ProgramaResponseDto.class)).toList();
        return ResponseEntity.ok(programas);
    }

    @GetMapping("/{id}/voluntarios")
    public ResponseEntity<List<VoluntarioResponseDto>> getAllVoluntariosInPrograma(@PathVariable Long id) {
        List<VoluntarioResponseDto> voluntarios = programaService.getAllVoluntariosInPrograma(id);
        return ResponseEntity.ok(voluntarios);
    }

    @PostMapping("/{id}")
    public ResponseEntity<NewIdDTO> createPrograma(@PathVariable Long AlbergueId, @RequestBody @Valid ProgramaRequestDto programaRequestDto) {
        NewIdDTO programa = programaService.createPrograma(AlbergueId, programaRequestDto);
        return ResponseEntity.created(null).body(programa);
    }

    @PostMapping("/{id}/inscripcion")
    public ResponseEntity<Void> inscribirVoluntarioAPrograma(@PathVariable Long id, @RequestParam Long voluntarioId) {
        programaService.inscribirVoluntarioAPrograma(id, voluntarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/inscripcion")
    public ResponseEntity<Void> desinscribirVoluntarioDePrograma(@PathVariable Long id, @RequestParam Long voluntarioId) {
        programaService.desinscribirVoluntarioDePrograma(id, voluntarioId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrograma(@PathVariable Long id) {
        programaService.deletePrograma(id);
        return ResponseEntity.noContent().build();
    }
}
