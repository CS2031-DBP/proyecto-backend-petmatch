package org.example.petmatch.volunteerProgram;

import org.example.petmatch.Common.NewIdDTO;
import org.example.petmatch.Email.EmailService;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramResponseDto;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VolunteerProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private VolunteerProgramService volunteerProgramService;


    @Test
    @WithMockUser
    void ShouldReturn200AndListWhenGetAllPrograms() throws Exception {
        var dto = new VolunteerProgramResponseDto();
        dto.setId(1L);
        dto.setNombre("Campaña de donación");

        when(volunteerProgramService.getAllProgramas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/programas").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Campaña de donación"));
    }


    @WithMockUser(username = "user@demo.com")
    @Test
    void ShouldEnrollVolunteerWhenAuthenticated() throws Exception {
        doNothing().when(volunteerProgramService).enrollingVolunteerPrograma(10L, 7L);

        mockMvc.perform(post("/programas/10/inscripcion")
                        .param("voluntarioId", "7"))
                .andExpect(status().isOk());

        verify(volunteerProgramService).enrollingVolunteerPrograma(10L, 7L);
    }


    @WithMockUser(username = "vol@demo.com")
    @Test
    void ShouldCancelOwnEnrollmentWhenAuthenticated() throws Exception {
        doNothing().when(volunteerProgramService)
                .desinscribirDePrograma(15L, "vol@demo.com");

        mockMvc.perform(delete("/programas/15/insctipciones"))
                .andExpect(status().isNoContent());

        verify(volunteerProgramService).desinscribirDePrograma(15L, "vol@demo.com");
    }


    @WithMockUser(username = "admin@demo.com", roles = {"ADMIN"})
    @Test
    void ShouldDeleteWhenAdminRole() throws Exception {
        doNothing().when(volunteerProgramService).deletePrograma(99L);

        mockMvc.perform(delete("/programas/99"))
                .andExpect(status().isNoContent());

        verify(volunteerProgramService).deletePrograma(99L);
    }


    @WithMockUser(username = "albergue@demo.com", roles = {"ALBERGUE"})
    @Test
    void ShouldDeleteWhenAlbergueIsOwner() throws Exception {
        doNothing().when(volunteerProgramService)
                .albergueDeletePrograma(21L, "albergue@demo.com");

        mockMvc.perform(delete("/programas/albergue/21"))
                .andExpect(status().isNoContent());

        verify(volunteerProgramService).albergueDeletePrograma(21L, "albergue@demo.com");
    }
}
