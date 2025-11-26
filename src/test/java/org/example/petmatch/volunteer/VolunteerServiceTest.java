package org.example.petmatch.volunteer;


import org.example.petmatch.Volunteer.Domain.Volunteer;
import org.example.petmatch.Volunteer.Domain.VolunteerService;
import org.example.petmatch.Volunteer.Infraestructure.VolunteerRepository;
import org.example.petmatch.Volunteer.dto.VolunteerResponseDto;
import org.example.petmatch.Volunteer.exception.VolunteerNotFoundException;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramResponseDto;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VolunteerServiceTest {

    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VolunteerService volunteerService;

    @Test
    void shouldReturnAllVolunteersWhenRepositoryHasData() {
        Volunteer v1 = new Volunteer();
        v1.setId(1L);
        v1.setName("Juan");
        Volunteer v2 = new Volunteer();
        v2.setId(2L);
        v2.setName("Ana");

        when(volunteerRepository.findAll()).thenReturn(List.of(v1, v2));

        VolunteerResponseDto d1 = new VolunteerResponseDto();
        d1.setId(1L); d1.setName("Juan");
        VolunteerResponseDto d2 = new VolunteerResponseDto();
        d2.setId(2L); d2.setName("Ana");

        when(modelMapper.map(v1, VolunteerResponseDto.class)).thenReturn(d1);
        when(modelMapper.map(v2, VolunteerResponseDto.class)).thenReturn(d2);


        List<VolunteerResponseDto> result = volunteerService.getAllVoluntarios();


        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getName());
        assertEquals("Ana", result.get(1).getName());
        verify(volunteerRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(Volunteer.class), eq(VolunteerResponseDto.class));
    }

    @Test
    void shouldReturnProgramsWhenVolunteerExists() {
        Volunteer volunteer = mock(Volunteer.class);
        volunteer.setId(10L);

        VolunteerProgram p1 = new VolunteerProgram();
        VolunteerProgram p2 = new VolunteerProgram();

        when(volunteer.getPrograms()).thenReturn(List.of(p1, p2));
        when(volunteerRepository.findById(10L)).thenReturn(Optional.of(volunteer));

        VolunteerProgramResponseDto r1 = new VolunteerProgramResponseDto();
        r1.setId(100L); r1.setNombre("Programa A");
        VolunteerProgramResponseDto r2 = new VolunteerProgramResponseDto();
        r2.setId(200L); r2.setNombre("Programa B");

        when(modelMapper.map(p1, VolunteerProgramResponseDto.class)).thenReturn(r1);
        when(modelMapper.map(p2, VolunteerProgramResponseDto.class)).thenReturn(r2);


        List<VolunteerProgramResponseDto> result = volunteerService.getProgramaByVoluntarioId(10L);

        assertEquals(2, result.size());
        assertEquals("Programa A", result.get(0).getNombre());
        assertEquals("Programa B", result.get(1).getNombre());
        verify(volunteerRepository).findById(10L);
        verify(modelMapper, times(2)).map(any(VolunteerProgram.class), eq(VolunteerProgramResponseDto.class));
    }

    @Test
    void shouldReturnEmptyListWhenVolunteerHasNoPrograms() {
        Volunteer volunteer = mock(Volunteer.class);
        when(volunteer.getPrograms()).thenReturn(List.of());
        when(volunteerRepository.findById(77L)).thenReturn(Optional.of(volunteer));

        List<VolunteerProgramResponseDto> result = volunteerService.getProgramaByVoluntarioId(77L);

        assertTrue(result.isEmpty());
        verify(volunteerRepository).findById(77L);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void shouldThrowExceptionWhenVolunteerNotFound() {
        when(volunteerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(VolunteerNotFoundException.class,
                () -> volunteerService.getProgramaByVoluntarioId(999L));

        verify(volunteerRepository).findById(999L);
        verifyNoInteractions(modelMapper);
    }

}
