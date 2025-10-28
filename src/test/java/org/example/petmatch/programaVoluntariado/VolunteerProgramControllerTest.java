package org.example.petmatch.programaVoluntariado;

import org.example.petmatch.Shelter.Exceptions.ShelterNotFoundException;
import org.example.petmatch.Common.NewIdDTO;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramRequestDto;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramResponseDto;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramService;
import org.example.petmatch.Volunteer_Program.exception.VolunteerProgramNotFoundException;
import org.example.petmatch.Volunteer.dto.VolunteerResponseDto;
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
class VolunteerProgramControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VolunteerProgramService volunteerProgramService;

    @Test
    @WithMockUser
    public void ShouldReturnOkWhenProgramasExiste() throws Exception {
        VolunteerProgramResponseDto p1 = new VolunteerProgramResponseDto();
        p1.setId(1L);
        p1.setNombre("Programa 1");
        p1.setDescripcion("Descripcion 1");
        p1.setFechaInicio(ZonedDateTime.now().minusDays(10));
        p1.setFechaFin(ZonedDateTime.now().plusDays(10));

        VolunteerProgramResponseDto p2 = new VolunteerProgramResponseDto();
        p2.setId(2L);
        p2.setNombre("Programa 2");
        p2.setDescripcion("Descripcion 2");
        p2.setFechaInicio(ZonedDateTime.now().minusDays(5));
        p2.setFechaFin(ZonedDateTime.now().plusDays(15));

        List<VolunteerProgramResponseDto> programas = Arrays.asList(p1, p2);

        when(volunteerProgramService.getAllProgramas()).thenReturn(programas);

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
        when(volunteerProgramService.getAllProgramas()).thenReturn(List.of());

        mockMvc.perform(get("/programas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @WithMockUser
    public void ShouldReturnOkWhenProgramaHasVoluntarios() throws Exception {
        Long programaId = 1L;

        VolunteerResponseDto v1 = new VolunteerResponseDto();
        v1.setId(1L);
        v1.setName("Voluntario 1");
        v1.setLastname("Apellido 1");
        v1.setEmail("vol1@gmail.com");
        VolunteerResponseDto v2 = new VolunteerResponseDto();
        v2.setId(2L);
        v2.setName("Voluntario 2");
        v2.setLastname("Apellido 2");
        v2.setEmail("vol2@gmail.com");
        List<VolunteerResponseDto> voluntarios = Arrays.asList(v1, v2);

        when(volunteerProgramService.getAllVoluntariosInPrograma(programaId)).thenReturn(voluntarios);

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

        when(volunteerProgramService.getAllVoluntariosInPrograma(programaId)).thenReturn(List.of());

        mockMvc.perform(get("/programas/{id}/voluntarios", programaId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @WithMockUser
    public void ShouldReturnNotFoundWhenProgramaDoesNotExist() throws Exception {
        Long programaId = 99L;

        when(volunteerProgramService.getAllVoluntariosInPrograma(programaId))
                .thenThrow(new VolunteerProgramNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"));

        mockMvc.perform(get("/programas/{id}/voluntarios", programaId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "example@gmail.com")
    public void ShouldCreateProgramaWhenDataIsValid() throws Exception {
        VolunteerProgramRequestDto volunteerProgramRequestDto = new VolunteerProgramRequestDto();
        volunteerProgramRequestDto.setNombre("Nuevo Programa");
        volunteerProgramRequestDto.setDescripcion("Descripcion del nuevo programa");
        volunteerProgramRequestDto.setFechaInicio(ZonedDateTime.now().plusDays(1));
        volunteerProgramRequestDto.setFechaFin(ZonedDateTime.now().plusDays(10));
        volunteerProgramRequestDto.setNumeroVoluntariosNecesarios(5);

        NewIdDTO newIdDTO = new NewIdDTO(1L);

        when(volunteerProgramService.createPrograma(anyString(), any(VolunteerProgramRequestDto.class)))
                .thenReturn(newIdDTO);

        mockMvc.perform(post("/programas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(volunteerProgramRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithMockUser(username = "ex@gmail.com")
    public void ShouldReturnBadRequestWhenCreatingProgramaWithInvalidData() throws Exception {
        VolunteerProgramRequestDto volunteerProgramRequestDto = new VolunteerProgramRequestDto();
        volunteerProgramRequestDto.setNombre(""); // Nombre inválido
        volunteerProgramRequestDto.setDescripcion("Descripcion del nuevo programa");
        volunteerProgramRequestDto.setFechaInicio(ZonedDateTime.now().plusDays(10));
        volunteerProgramRequestDto.setFechaFin(ZonedDateTime.now().plusDays(1)); // Fecha fin antes de fecha inicio
        volunteerProgramRequestDto.setNumeroVoluntariosNecesarios(-5); // Número inválido

        mockMvc.perform(post("/programas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(volunteerProgramRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "ex@gmail.com")
    public void ShouldReturnNotFoundWhenCreatingProgramaWithNonExistentAlbergue() throws Exception {
        VolunteerProgramRequestDto volunteerProgramRequestDto = new VolunteerProgramRequestDto();
        volunteerProgramRequestDto.setNombre("Nuevo Programa");
        volunteerProgramRequestDto.setDescripcion("Descripcion del nuevo programa");
        volunteerProgramRequestDto.setFechaInicio(ZonedDateTime.now().plusDays(1));
        volunteerProgramRequestDto.setFechaFin(ZonedDateTime.now().plusDays(10));
        volunteerProgramRequestDto.setNumeroVoluntariosNecesarios(5);

        when(volunteerProgramService.createPrograma(anyString(), any(VolunteerProgramRequestDto.class)))
                .thenThrow(new ShelterNotFoundException("Albergue no encontrado"));

        mockMvc.perform(post("/programas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(volunteerProgramRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void ShouldInscribirVoluntarioWhenDataIsValid() throws Exception {
        Long programaId = 1L;
        Long voluntarioId = 2L;

        doNothing().when(volunteerProgramService).enrollingVolunteerPrograma(programaId, voluntarioId);

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

        doThrow(new VolunteerProgramNotFoundException("Programa de voluntariado con id " + programaId + " no encontrado"))
                .when(volunteerProgramService).enrollingVolunteerPrograma(programaId, voluntarioId);

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

        doThrow(new VolunteerProgramNotFoundException("Voluntario con id " + voluntarioId + " no encontrado"))
                .when(volunteerProgramService).enrollingVolunteerPrograma(programaId, voluntarioId);

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

        doNothing().when(volunteerProgramService).unsubscribeVolunteerProgram(programaId, voluntarioId);

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
    public void ShouldUnsubscribeVolunteerProgramAlbergueWhenDataIsValid() throws Exception {
        Long programaId = 1L;
        Long voluntarioId = 2L;

        doNothing().when(volunteerProgramService).unsubscribeVolunteerProgramOnShelter(programaId, voluntarioId, "albergue@gmail.com");
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

        doNothing().when(volunteerProgramService).desinscribirDePrograma(programaId, "vol@gmail.com");
        mockMvc.perform(delete("/programas/{programaId}/insctipciones", programaId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void ShouldAdminDeleteProgramaWhenDataIsValid() throws Exception {
        Long programaId = 1L;

        doNothing().when(volunteerProgramService).deletePrograma(programaId);
        mockMvc.perform(delete("/programas/{programaId}", programaId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ALBERGUE"}, username = "alb@gmail.com")
    public void ShouldAlbergueDeleteProgramaWhenDataIsValid() throws Exception {
        Long programaId = 1L;

        doNothing().when(volunteerProgramService).albergueDeletePrograma(programaId, "alb@gmail.com");
        mockMvc.perform(delete("/programas/albergue/{programaId}", programaId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }


}
