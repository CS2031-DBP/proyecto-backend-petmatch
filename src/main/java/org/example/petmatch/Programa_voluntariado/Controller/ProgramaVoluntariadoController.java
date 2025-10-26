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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @PostMapping("/{AlbergueId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NewIdDTO> createPrograma(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid ProgramaRequestDto programaRequestDto) {
        NewIdDTO programa = programaService.createPrograma(userDetails.getUsername(), programaRequestDto);
        URI location = URI.create(String.format("/programas/%s", programa.getId()));
        return ResponseEntity.created(location).body(programa);
    }

    @PostMapping("/{id}/inscripcion")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> inscribirVoluntarioAPrograma(@PathVariable Long id, @RequestParam Long voluntarioId) {
        programaService.inscribirVoluntarioAPrograma(id, voluntarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/inscripcion")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desinscribirVoluntarioDePrograma(@PathVariable Long id, @RequestParam Long voluntarioId) {
        programaService.desinscribirVoluntarioDePrograma(id, voluntarioId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/alberge/{programaid}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<Void> desinscribirVoluntarioDeProgramaAlbergue(@PathVariable Long programaid, @RequestParam Long voluntarioId, @AuthenticationPrincipal UserDetails userDetails) {
        programaService.desinscribirVoluntarioDeProgramaAlbergue(programaid, voluntarioId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{programaId}/insctipciones")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelarInscripcionEnPrograma(@PathVariable Long programaId, @AuthenticationPrincipal UserDetails userDetails) {
        programaService.desinscribirDePrograma(programaId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> adminDeletePrograma(@PathVariable Long id) {
        programaService.deletePrograma(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/albergue/{id}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<Void> albergueDeletePrograma(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        programaService.albergueDeletePrograma(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
