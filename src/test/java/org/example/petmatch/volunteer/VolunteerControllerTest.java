package org.example.petmatch.volunteer;


import org.example.petmatch.Volunteer.Controller.VolunteerController;
import org.example.petmatch.Volunteer.Domain.VolunteerService;
import org.example.petmatch.Volunteer.dto.VolunteerResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VolunteerControllerTest {

    @Mock
    private VolunteerService volunteerService;

    @Mock
    private Principal principal;

    @InjectMocks
    private VolunteerController volunteerController;

    @Test
    public void ShouldReturnAllVolunteersWhenGetAllVoluntarios() {
        var dto = new VolunteerResponseDto();
        dto.setId(1L);

        when(volunteerService.getAllVoluntarios()).thenReturn(List.of(dto));

        ResponseEntity<List<VolunteerResponseDto>> response = volunteerController.getAllVoluntarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(volunteerService).getAllVoluntarios();
    }


}
