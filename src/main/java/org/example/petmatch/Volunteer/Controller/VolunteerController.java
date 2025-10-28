package org.example.petmatch.Volunteer.Controller;

import lombok.AllArgsConstructor;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramResponseDto;
import org.example.petmatch.Volunteer.Domain.VolunteerService;
import org.example.petmatch.Volunteer.dto.VolunteerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/voluntarios")
@AllArgsConstructor
public class VolunteerController {
    private final VolunteerService volunteerService;

    @GetMapping
    public ResponseEntity<List<VolunteerResponseDto>> getAllVoluntarios() {
        List<VolunteerResponseDto> voluntarios = volunteerService.getAllVoluntarios();
        return ResponseEntity.ok(voluntarios);
    }

    @GetMapping("/{id}/programas")
    public ResponseEntity<List<VolunteerProgramResponseDto>> getProgramaByVoluntarioId(@PathVariable Long id) {
        List<VolunteerProgramResponseDto> programas = volunteerService.getProgramaByVoluntarioId(id);
        return ResponseEntity.ok(programas);
    }
}
