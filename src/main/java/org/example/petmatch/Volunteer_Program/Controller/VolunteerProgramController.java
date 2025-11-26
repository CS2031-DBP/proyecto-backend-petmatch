package org.example.petmatch.Volunteer_Program.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petmatch.Common.NewIdDTO;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramRequestDto;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramResponseDto;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramService;
import org.example.petmatch.Volunteer.dto.VolunteerResponseDto;
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
public class VolunteerProgramController {
    private final VolunteerProgramService volunteerProgramService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<VolunteerProgramResponseDto>> getAllProgramas() {
        List<VolunteerProgramResponseDto> programas = volunteerProgramService.getAllProgramas().stream()
                .map(programa -> modelMapper.map(programa, VolunteerProgramResponseDto.class))
                .toList();
        return ResponseEntity.ok(programas);
    }

    @GetMapping("/{id}/voluntarios")
    public ResponseEntity<List<VolunteerResponseDto>> getAllVoluntariosInPrograma(@PathVariable Long id) {
        List<VolunteerResponseDto> voluntarios = volunteerProgramService.getAllVoluntariosInPrograma(id);
        return ResponseEntity.ok(voluntarios);
    }

    // SOLO ALBERGUES pueden crear programas
    @PostMapping
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<NewIdDTO> createPrograma(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid VolunteerProgramRequestDto volunteerProgramRequestDto) {
        NewIdDTO programa = volunteerProgramService.createPrograma(userDetails.getUsername(), volunteerProgramRequestDto);
        URI location = URI.create(String.format("/programas/%s", programa.getId()));
        return ResponseEntity.created(location).body(programa);
    }

    // CORREGIDO: El usuario se inscribe a sí mismo
    @PostMapping("/{id}/inscripcion")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> enrollingVolunteerProgram(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        volunteerProgramService.enrollingVolunteerPrograma(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    // ALBERGUE da de baja a un voluntario
    @DeleteMapping("/albergue/{programaid}/voluntario/{voluntarioId}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<Void> unsubscribeVolunteerProgramOnShelter(
            @PathVariable Long programaid,
            @PathVariable Long voluntarioId,
            @AuthenticationPrincipal UserDetails userDetails) {
        volunteerProgramService.unsubscribeVolunteerProgramOnShelter(programaid, voluntarioId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // USUARIO se da de baja a sí mismo
    @DeleteMapping("/{programaId}/inscripciones")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> cancellInscriptionProgram(
            @PathVariable Long programaId,
            @AuthenticationPrincipal UserDetails userDetails) {
        volunteerProgramService.desinscribirDePrograma(programaId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // ALBERGUE elimina su propio programa
    @DeleteMapping("/albergue/{id}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<Void> albergueDeletePrograma(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        volunteerProgramService.albergueDeletePrograma(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}