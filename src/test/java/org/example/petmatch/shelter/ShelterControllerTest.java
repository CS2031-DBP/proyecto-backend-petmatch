package org.example.petmatch.shelter;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.petmatch.Shelter.Controller.ShelterController;
import org.example.petmatch.Shelter.DTO.ShelterPresentationDTO;
import org.example.petmatch.Shelter.Domain.ShelterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShelterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShelterService shelterService;

    @Test
    public void ShouldReturnOkWhenAlberguesExist() throws Exception {
        ShelterPresentationDTO shelter1 = new ShelterPresentationDTO();
        shelter1.setName("Shelter One");
        shelter1.setAddress("123 Main St");
        shelter1.setPhone("+5198765432");
        shelter1.setAvailableSpaces(10);
        ShelterPresentationDTO shelter2 = new ShelterPresentationDTO();
        shelter2.setName("Shelter Two");
        shelter2.setAddress("456 Elm St");
        shelter2.setPhone("+5191234567");
        shelter2.setAvailableSpaces(5);
        when(shelterService.findAll()).thenReturn(List.of(shelter1, shelter2));

        mockMvc.perform(get("/albergues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(shelter1, shelter2))));
    }


}
