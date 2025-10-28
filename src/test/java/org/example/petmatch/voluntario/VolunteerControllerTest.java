package org.example.petmatch.voluntario;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramResponseDto;
import org.example.petmatch.Volunteer.Domain.VolunteerService;
import org.example.petmatch.Volunteer.dto.VolunteerResponseDto;
import org.example.petmatch.Volunteer.exception.VolunteerNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;


import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VolunteerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    VolunteerService volunteerService;

    @Test
    @WithMockUser
    public void ShouldReturnOkWhenVoluntariosExist() throws Exception {
        VolunteerResponseDto voluntario1 = new VolunteerResponseDto();
        voluntario1.setId(1L);
        voluntario1.setName("Juan");
        voluntario1.setLastname("Perez");
        voluntario1.setEmail("jp@gmail.com");
        VolunteerResponseDto voluntario2 = new VolunteerResponseDto();
        voluntario2.setId(2L);
        voluntario2.setName("Ana");
        voluntario2.setLastname("Gomez");
        voluntario2.setEmail("ag@gmail.com");
        when(volunteerService.getAllVoluntarios()).thenReturn(List.of(voluntario1, voluntario2));
        mockMvc.perform(get("/voluntarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(voluntario1, voluntario2))));
    }

    @Test
    @WithMockUser
    public void ShouldReturnEmptyListWhenNoVoluntariosExist() throws Exception {
        when(volunteerService.getAllVoluntarios()).thenReturn(List.of());
        mockMvc.perform(get("/voluntarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    @Test
    @WithMockUser
    public void ShouldReturnOkWhenProgramasExistForVoluntario() throws Exception {
        VolunteerProgramResponseDto programa1 = new VolunteerProgramResponseDto();
        programa1.setId(1L);
        programa1.setNombre("Programa 1");
        programa1.setDescripcion("Descripcion 1");
        VolunteerProgramResponseDto programa2 = new VolunteerProgramResponseDto();
        programa2.setId(2L);
        programa2.setNombre("Programa 2");
        programa2.setDescripcion("Descripcion 2");
        when(volunteerService.getProgramaByVoluntarioId(anyLong())).thenReturn(List.of(programa1, programa2));
        mockMvc.perform(get("/voluntarios/1/programas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(programa1, programa2))));
    }

    @Test
    @WithMockUser
    public void ShouldReturnNotFoundWhenVoluntarioDoesNotExist() throws Exception {
        when(volunteerService.getProgramaByVoluntarioId(anyLong())).thenThrow(new VolunteerNotFoundException("Voluntario not found"));
        mockMvc.perform(get("/voluntarios/999/programas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void ShouldReturnEmptyListWhenNoProgramasForVoluntario() throws Exception {
        when(volunteerService.getProgramaByVoluntarioId(anyLong())).thenReturn(List.of());
        mockMvc.perform(get("/voluntarios/1/programas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}

