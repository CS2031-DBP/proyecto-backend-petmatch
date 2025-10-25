package org.example.petmatch.Voluntario.Controller;

import lombok.AllArgsConstructor;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaResponseDto;
import org.example.petmatch.Voluntario.Domain.VoluntarioService;
import org.example.petmatch.Voluntario.dto.VoluntarioResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/voluntarios")
@AllArgsConstructor
public class VoluntarioController {
    private final VoluntarioService voluntarioService;

    @GetMapping
    public ResponseEntity<List<VoluntarioResponseDto>> getAllVoluntarios() {
        List<VoluntarioResponseDto> voluntarios = voluntarioService.getAllVoluntarios();
        return ResponseEntity.ok(voluntarios);
    }

    @GetMapping("/{id}/programas")
    public ResponseEntity<List<ProgramaResponseDto>> getProgramaByVoluntarioId(@PathVariable Long id) {
        List<ProgramaResponseDto> programas = voluntarioService.getProgramaByVoluntarioId(id);
        return ResponseEntity.ok(programas);
    }
}
