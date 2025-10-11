package Programa_voluntariado.Controller;


import Programa_voluntariado.DTO.ProgramaResponseDto;
import Programa_voluntariado.Domain.ProgramaService;
import User.dto.UserResponseDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
