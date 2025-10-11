package Programa_voluntariado.Controller;


import Common.NewIdDTO;
import Programa_voluntariado.DTO.ProgramaRequestDto;
import Programa_voluntariado.DTO.ProgramaResponseDto;
import Programa_voluntariado.Domain.ProgramaService;
import User.dto.UserResponseDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programas")
@NoArgsConstructor
public class ProgramaVoluntariadoController {

    @Autowired
    private ProgramaService programaService;

    @GetMapping
    public ResponseEntity<List<ProgramaResponseDto>> getAllProgramas() {
        List<ProgramaResponseDto> programas = programaService.getAllProgramas();
        return ResponseEntity.ok(programas);
    }

    @GetMapping("/{id}/voluntarios")
    public ResponseEntity<List<UserResponseDto>> getAllVoluntariosInPrograma(@PathVariable Long id) {
        List<UserResponseDto> voluntarios = programaService.getAllVoluntariosInPrograma(id);
        return ResponseEntity.ok(voluntarios);
    }

    @PostMapping("/{id}")
    public ResponseEntity<NewIdDTO> createPrograma(@PathVariable Long AlbergueId, @RequestBody @Validated ProgramaRequestDto programaRequestDto) {
        NewIdDTO programa = programaService.createPrograma(AlbergueId, programaRequestDto);
        return ResponseEntity.created(null).body(programa);
    }

    @PostMapping("/{id}/inscripcion")
    public ResponseEntity<Void> inscribirVoluntarioAPrograma(@PathVariable Long id, @RequestParam Long voluntarioId) {
        programaService.inscribirVoluntarioAPrograma(id, voluntarioId);
        return ResponseEntity.ok().build();
    }

}
