package org.example.petmatch.programaVoluntariado;

import org.example.petmatch.Common.NewIdDTO;
import org.example.petmatch.GlobalExceptionHandler;
import org.example.petmatch.Programa_voluntariado.Controller.ProgramaVoluntariadoController;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaRequestDto;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaResponseDto;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaService;
import org.example.petmatch.Programa_voluntariado.exception.ProgramaNotFoundException;
import org.example.petmatch.Voluntario.dto.VoluntarioResponseDto;
import org.example.petmatch.config.ModelMapperconfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProgramaVoluntariadoController.class)
@Import({GlobalExceptionHandler.class, ModelMapperconfig.class})
public class ProgramaControllerTest {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    ProgramaService programaService;

    @Test
    @DisplayName("shouldReturnProgramasWhenGetAllProgramas")
    void shouldReturnProgramasWhenGetAllProgramas() throws Exception {
        var p1 = new ProgramaResponseDto();
        p1.setId(1L); p1.setNombre("Rescate");
        var p2 = new ProgramaResponseDto();
        p2.setId(2L); p2.setNombre("Adopciones");

        when(programaService.getAllProgramas()).thenReturn(List.of(p1, p2));

        mvc.perform(get("/programas").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Rescate"))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("shouldReturnVoluntariosWhenProgramaExists")
    void shouldReturnVoluntariosWhenProgramaExists() throws Exception {
        var v1 = new VoluntarioResponseDto();
        v1.setId(10L); v1.setName("Ana");
        var v2 = new VoluntarioResponseDto();
        v2.setId(11L); v2.setName("Bruno");

        when(programaService.getAllVoluntariosInPrograma(1L)).thenReturn(List.of(v1, v2));

        mvc.perform(get("/programas/1/voluntarios").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Ana"));
    }

    @Test
    @DisplayName("shouldReturn404WhenProgramaNotFoundInVoluntarios")
    void shouldReturn404WhenProgramaNotFoundInVoluntarios() throws Exception {
        when(programaService.getAllVoluntariosInPrograma(99L))
                .thenThrow(new ProgramaNotFoundException("nope"));

        mvc.perform(get("/programas/99/voluntarios"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("shouldCreateProgramaWhenAuthenticated")
    void shouldCreateProgramaWhenAuthenticated() throws Exception {
        var now = ZonedDateTime.now();
        var reqJson = """
            {
              "nombre": "Vacunatón",
              "descripcion": "Campaña",
              "fechaInicio": "%s",
              "fechaFin": "%s",
              "numeroVoluntariosNecesarios": 10
            }
            """.formatted(now.plusDays(2), now.plusDays(7));

        var newId = new NewIdDTO(123L);
        when(programaService.createPrograma(eq("albergue@x.com"), any(ProgramaRequestDto.class)))
                .thenReturn(newId);

        mvc.perform(post("/programas/5") // {AlbergueId} dummy
                        .with(SecurityMockMvcRequestPostProcessors.user("albergue@x.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/programas/123"))
                .andExpect(jsonPath("$.id").value(123L));

        verify(programaService).createPrograma(eq("albergue@x.com"), any(ProgramaRequestDto.class));
    }

    @Test
    @DisplayName("shouldReturn401WhenCreateProgramaWithoutAuth")
    void shouldReturn401WhenCreateProgramaWithoutAuth() throws Exception {
        var now = ZonedDateTime.now();
        var reqJson = """
            {
              "nombre": "Vacunatón",
              "descripcion": "Campaña",
              "fechaInicio": "%s",
              "fechaFin": "%s",
              "numeroVoluntariosNecesarios": 10
            }
            """.formatted(now.plusDays(2), now.plusDays(7));

        mvc.perform(post("/programas/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("shouldInscribirVoluntarioWhenAuthenticated")
    void shouldInscribirVoluntarioWhenAuthenticated() throws Exception {
        mvc.perform(post("/programas/10/inscripcion")
                        .with(SecurityMockMvcRequestPostProcessors.user("user@x.com"))
                        .param("voluntarioId", "200"))
                .andExpect(status().isOk());

        verify(programaService).inscribirVoluntarioAPrograma(10L, 200L);
    }

    @Test
    @DisplayName("shouldReturn401WhenInscribirWithoutAuth")
    void shouldReturn401WhenInscribirWithoutAuth() throws Exception {
        mvc.perform(post("/programas/10/inscripcion").param("voluntarioId", "200"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("shouldDesinscribirVoluntarioWhenAdmin")
    void shouldDesinscribirVoluntarioWhenAdmin() throws Exception {
        mvc.perform(delete("/programas/10/inscripcion")
                        .param("voluntarioId", "200")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@x.com").roles("ADMIN")))
                .andExpect(status().isNoContent());

        verify(programaService).desinscribirVoluntarioDePrograma(10L, 200L);
    }

    @Test
    @DisplayName("shouldReturn403WhenDesinscribirWithoutAdminRole")
    void shouldReturn403WhenDesinscribirWithoutAdminRole() throws Exception {
        mvc.perform(delete("/programas/10/inscripcion")
                        .param("voluntarioId", "200")
                        .with(SecurityMockMvcRequestPostProcessors.user("user@x.com")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("shouldDesinscribirVoluntarioWhenAlbergueRole")
    void shouldDesinscribirVoluntarioWhenAlbergueRole() throws Exception {
        mvc.perform(delete("/programas/alberge/77")
                        .param("voluntarioId", "200")
                        .with(SecurityMockMvcRequestPostProcessors.user("alb@x.com").roles("ALBERGUE")))
                .andExpect(status().isNoContent());

        verify(programaService).desinscribirVoluntarioDeProgramaAlbergue(77L, 200L, "alb@x.com");
    }

    @Test
    @DisplayName("shouldCancelarInscripcionWhenAuthenticated")
    void shouldCancelarInscripcionWhenAuthenticated() throws Exception {
        mvc.perform(delete("/programas/55/insctipciones")
                        .with(SecurityMockMvcRequestPostProcessors.user("user@x.com")))
                .andExpect(status().isNoContent());

        verify(programaService).desinscribirDePrograma(55L, "user@x.com");
    }

    @Test
    @DisplayName("shouldReturn401WhenCancelarInscripcionWithoutAuth")
    void shouldReturn401WhenCancelarInscripcionWithoutAuth() throws Exception {
        mvc.perform(delete("/programas/55/insctipciones"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("shouldAdminDeleteProgramaWhenAdminRole")
    void shouldAdminDeleteProgramaWhenAdminRole() throws Exception {
        mvc.perform(delete("/programas/10")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@x.com").roles("ADMIN")))
                .andExpect(status().isNoContent());

        verify(programaService).deletePrograma(10L);
    }

    @Test
    @DisplayName("shouldReturn403WhenAdminDeleteWithoutAdminRole")
    void shouldReturn403WhenAdminDeleteWithoutAdminRole() throws Exception {
        mvc.perform(delete("/programas/10")
                        .with(SecurityMockMvcRequestPostProcessors.user("user@x.com")))
                .andExpect(status().isForbidden());
    }

}
