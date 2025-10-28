package org.example.petmatch.programaVoluntariado;

import org.example.petmatch.Albergue.Exceptions.AlbergueNotFoundException;
import org.example.petmatch.Common.NewIdDTO;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaRequestDto;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaResponseDto;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaService;
import org.example.petmatch.Programa_voluntariado.exception.ProgramaNotFoundException;
import org.example.petmatch.Voluntario.dto.VoluntarioResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ProgramaVoluntariadoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProgramaService programaService;

    @Test
    @WithMockUser
    public void ShouldReturnOkWhenProgramasExiste() throws Exception {
        ProgramaResponseDto p1 = new ProgramaResponseDto();
        p1.setId(1L);
        p1.setNombre("Programa 1");
        p1.setDescripcion("Descripcion 1");
        p1.setFechaInicio(ZonedDateTime.now().minusDays(10));
        p1.setFechaFin(ZonedDateTime.now().plusDays(10));

        ProgramaResponseDto p2 = new ProgramaResponseDto();
        p2.setId(2L);
        p2.setNombre("Programa 2");
        p2.setDescripcion("Descripcion 2");
        p2.setFechaInicio(ZonedDateTime.now().minusDays(5));
        p2.setFechaFin(ZonedDateTime.now().plusDays(15));

        List<ProgramaResponseDto> programas = Arrays.asList(p1, p2);

        when(programaService.getAllProgramas()).thenReturn(programas);

        mockMvc.perform(get("/programas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].nombre", is("Programa 1")))
                .andExpect(jsonPath("$[1].nombre", is("Programa 2")));
    }

    @Test
    @WithMockUser
    public void ShouldReturnEmptyListWhenNoProgramasExist() throws Exception {
        when(programaService.getAllProgramas()).thenReturn(List.of());

        mockMvc.perform(get("/programas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @WithMockUser
    public void ShouldReturnOkWhenProgramaHasVoluntarios() throws Exception {
        Long programaId = 1L;

        VoluntarioResponseDto v1 = new VoluntarioResponseDto();
        v1.setId(1L);
        v1.setName("Voluntario 1");
        v1.setLastname("Apellido 1");
        v1.setEmail("vol1@gmail.com");
        VoluntarioResponseDto v2 = new VoluntarioResponseDto();
        v2.setId(2L);
        v2.setName("Voluntario 2");
        v2.setLastname("Apellido 2");
        v2.setEmail("vol2@gmail.com");
        List<VoluntarioResponseDto> voluntarios = Arrays.asList(v1, v2);

        when(programaService.getAllVoluntariosInPrograma(programaId)).thenReturn(voluntarios);

        mockMvc.perform(get("/programas/{id}/voluntarios", programaId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Voluntario 1")))
                .andExpect(jsonPath("$[1].email", is("vol2@gmail.com")));
    }

    @Test
    @WithMockUser
    public void ShouldReturnEmptyListWhenProgramaHasNoVoluntarios() throws Exception {
        Long programaId = 1L;

        when(programaService.getAllVoluntariosInPrograma(programaId)).thenReturn(List.of());

        mockMvc.perform(get("/programas/{id}/voluntarios", programaId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @WithMockUser
    public void ShouldReturnNotFoundWhenProgramaDoesNotExist() throws Exception {
        Long programaId = 99L;

        when(programaService.getAllVoluntariosInPrograma(programaId))
                .thenThrow(new ProgramaNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));

        mockMvc.perform(get("/programas/{id}/voluntarios", programaId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "example@gmail.com")
    public void ShouldCreateProgramaWhenDataIsValid() throws Exception {
        ProgramaRequestDto programaRequestDto = new ProgramaRequestDto();
        programaRequestDto.setNombre("Nuevo Programa");
        programaRequestDto.setDescripcion("Descripcion del nuevo programa");
        programaRequestDto.setFechaInicio(ZonedDateTime.now().plusDays(1));
        programaRequestDto.setFechaFin(ZonedDateTime.now().plusDays(10));
        programaRequestDto.setNumeroVoluntariosNecesarios(5);

        NewIdDTO newIdDTO = new NewIdDTO(1L);

        when(programaService.createPrograma(anyString(), any(ProgramaRequestDto.class)))
                .thenReturn(newIdDTO);

        mockMvc.perform(post("/programas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(programaRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithMockUser(username = "ex@gmail.com")
    public void ShouldReturnBadRequestWhenCreatingProgramaWithInvalidData() throws Exception {
        ProgramaRequestDto programaRequestDto = new ProgramaRequestDto();
        programaRequestDto.setNombre(""); // Nombre inválido
        programaRequestDto.setDescripcion("Descripcion del nuevo programa");
        programaRequestDto.setFechaInicio(ZonedDateTime.now().plusDays(10));
        programaRequestDto.setFechaFin(ZonedDateTime.now().plusDays(1)); // Fecha fin antes de fecha inicio
        programaRequestDto.setNumeroVoluntariosNecesarios(-5); // Número inválido

        mockMvc.perform(post("/programas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(programaRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "ex@gmail.com")
    public void ShouldReturnNotFoundWhenCreatingProgramaWithNonExistentAlbergue() throws Exception {
        ProgramaRequestDto programaRequestDto = new ProgramaRequestDto();
        programaRequestDto.setNombre("Nuevo Programa");
        programaRequestDto.setDescripcion("Descripcion del nuevo programa");
        programaRequestDto.setFechaInicio(ZonedDateTime.now().plusDays(1));
        programaRequestDto.setFechaFin(ZonedDateTime.now().plusDays(10));
        programaRequestDto.setNumeroVoluntariosNecesarios(5);

        when(programaService.createPrograma(anyString(), any(ProgramaRequestDto.class)))
                .thenThrow(new AlbergueNotFoundException("Albergue no encontrado"));

        mockMvc.perform(post("/programas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(programaRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void ShouldInscribirVoluntarioWhenDataIsValid() throws Exception {
        Long programaId = 1L;
        Long voluntarioId = 2L;

        doNothing().when(programaService).inscribirVoluntarioAPrograma(programaId, voluntarioId);

        mockMvc.perform(post("/programas/{programaID}/inscripcion", programaId)
                        .with(csrf())
                        .param("voluntarioId", voluntarioId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void ShouldReturnNotFoundWhenInscribingVoluntarioToNonExistentPrograma() throws Exception {
        Long programaId = 99L;
        Long voluntarioId = 2L;

        doThrow(new ProgramaNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"))
                .when(programaService).inscribirVoluntarioAPrograma(programaId, voluntarioId);

        mockMvc.perform(post("/programas/{programaID}/inscripcion", programaId)
                        .with(csrf())
                        .param("voluntarioId", voluntarioId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void ShouldReturnNotFoundWhenInscribingNonExistentVoluntarioToPrograma() throws Exception {
        Long programaId = 1L;
        Long voluntarioId = 99L;

        doThrow(new ProgramaNotFoundException("Voluntario con id " + voluntarioId + " no encontrado"))
                .when(programaService).inscribirVoluntarioAPrograma(programaId, voluntarioId);

        mockMvc.perform(post("/programas/{programaID}/inscripcion", programaId)
                        .with(csrf())
                        .param("voluntarioId", voluntarioId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void ShouldDesinscribirVoluntarioWhenDataIsValid() throws Exception {
        Long programaId = 1L;
        Long voluntarioId = 2L;

        doNothing().when(programaService).desinscribirVoluntarioDePrograma(programaId, voluntarioId);

        mockMvc.perform(delete("/programas/{programaID}/inscripcion", programaId)
                        .with(csrf())
                        .param("voluntarioId", voluntarioId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void ShouldReturnForbiddenWhenDesinscribingVoluntarioWithoutAdminRole() throws Exception {
        Long programaId = 1L;
        Long voluntarioId = 2L;

        mockMvc.perform(delete("/programas/{programaID}/inscripcion", programaId)
                        .with(csrf())
                        .param("voluntarioId", voluntarioId.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ALBERGUE"}, username = "albergue@gmail.com")
    public void ShouldDesinscribirVoluntarioDeProgramaAlbergueWhenDataIsValid() throws Exception {
        Long programaId = 1L;
        Long voluntarioId = 2L;

        doNothing().when(programaService).desinscribirVoluntarioDeProgramaAlbergue(programaId, voluntarioId, "albergue@gmail.com");
        mockMvc.perform(delete("/programas/albergue/{programaID}", programaId)
                        .with(csrf())
                        .param("voluntarioId", voluntarioId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    public void ShouldReturnForbiddenWhenDesinscribingVoluntarioDeProgramaAlbergueWithoutAlbergueRole() throws Exception {
        Long programaId = 1L;
        Long voluntarioId = 2L;

        mockMvc.perform(delete("/programas/albergue/{programaID}", programaId)
                        .with(csrf())
                        .param("voluntarioId", voluntarioId.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void ShouldCancelarInscripcionEnProgramaWhenDataIsValid() throws Exception {
        Long programaId = 1L;

        doNothing().when(programaService).desinscribirDePrograma(programaId, "vol@gmail.com");
        mockMvc.perform(delete("/programas/{programaId}/insctipciones", programaId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void ShouldAdminDeleteProgramaWhenDataIsValid() throws Exception {
        Long programaId = 1L;

        doNothing().when(programaService).deletePrograma(programaId);
        mockMvc.perform(delete("/programas/{programaId}", programaId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ALBERGUE"}, username = "alb@gmail.com")
    public void ShouldAlbergueDeleteProgramaWhenDataIsValid() throws Exception {
        Long programaId = 1L;

        doNothing().when(programaService).albergueDeletePrograma(programaId, "alb@gmail.com");
        mockMvc.perform(delete("/programas/albergue/{programaId}", programaId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }


}
