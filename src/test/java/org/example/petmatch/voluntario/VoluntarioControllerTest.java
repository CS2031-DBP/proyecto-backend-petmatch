package org.example.petmatch.voluntario;

import org.example.petmatch.Programa_voluntariado.DTO.ProgramaResponseDto;
import org.example.petmatch.Voluntario.Controller.VoluntarioController;
import org.example.petmatch.Voluntario.Domain.VoluntarioService;
import org.example.petmatch.Voluntario.dto.VoluntarioResponseDto;
import org.example.petmatch.Voluntario.exception.VoluntarioNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;


import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoluntarioController.class)
@ExtendWith(SpringExtension.class)
public class VoluntarioControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    VoluntarioService voluntarioService;

    @Test
    @DisplayName("shouldReturnListOfVoluntariosWhenGetAllVoluntariosIsCalled")
    void shouldReturnListOfVoluntariosWhenGetAllVoluntariosIsCalled() throws Exception {
        // given
        var v1 = new VoluntarioResponseDto();
        v1.setId(1L);
        v1.setName("Ana");
        v1.setLastname("Lopez");
        v1.setEmail("ana@test.com");

        var v2 = new VoluntarioResponseDto();
        v2.setId(2L);
        v2.setName("Bruno");
        v2.setLastname("Diaz");
        v2.setEmail("bruno@test.com");

        when(voluntarioService.getAllVoluntarios()).thenReturn(List.of(v1, v2));

        // when + then
        mockMvc.perform(get("/voluntarios")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Ana"))
                .andExpect(jsonPath("$[1].email").value("bruno@test.com"));
    }

    @Test
    @DisplayName("shouldReturnListOfProgramasWhenVoluntarioExists")
    void shouldReturnListOfProgramasWhenVoluntarioExists() throws Exception {
        // given
        var p1 = new ProgramaResponseDto();
        p1.setId(10L);
        p1.setNombre("Rescate");
        var p2 = new ProgramaResponseDto();
        p2.setId(11L);
        p2.setNombre("Adopciones");

        when(voluntarioService.getProgramaByVoluntarioId(1L)).thenReturn(List.of(p1, p2));

        // when + then
        mockMvc.perform(get("/voluntarios/1/programas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Rescate"))
                .andExpect(jsonPath("$[1].nombre").value("Adopciones"));
    }

    @Test
    @DisplayName("shouldReturn404WhenVoluntarioNotFound")
    void shouldReturn404WhenVoluntarioNotFound() throws Exception {
        // given
        when(voluntarioService.getProgramaByVoluntarioId(anyLong()))
                .thenThrow(new VoluntarioNotFoundException("Voluntario no encontrado con id: 99"));

        // when + then
        mockMvc.perform(get("/voluntarios/99/programas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

