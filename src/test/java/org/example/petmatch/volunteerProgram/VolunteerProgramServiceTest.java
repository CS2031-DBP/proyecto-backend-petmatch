package org.example.petmatch.volunteerProgram;

import org.example.petmatch.Inscription.domain.Inscription;
import org.example.petmatch.Inscription.exception.AlreadyEnrolledException;
import org.example.petmatch.Inscription.infrastructure.InscriptionRepository;
import org.example.petmatch.Shelter.Infraestructure.ShelterRepository;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.example.petmatch.Volunteer.Domain.Volunteer;
import org.example.petmatch.Volunteer.Infraestructure.VolunteerRepository;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramResponseDto;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramService;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramStatus;
import org.example.petmatch.Volunteer_Program.Infraestructure.VolunteerProgramRepository;
import org.example.petmatch.Volunteer_Program.exception.VolunteerProgramNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VolunteerProgramServiceTest {

    @Mock
    private VolunteerProgramRepository programaRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ShelterRepository shelterRepository;
    @Mock
    private VolunteerRepository volunteerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InscriptionRepository inscriptionRepository;

    @InjectMocks
    private VolunteerProgramService service;

    // -------- getAllProgramas ----------
    @Test
    public void ShouldReturnDtosWhenProgramsExist() {
        var entity = new VolunteerProgram();
        entity.setId(1L);
        entity.setName("Perritos");
        var dto = new VolunteerProgramResponseDto();
        dto.setId(1L);
        dto.setNombre("Perritos");

        when(programaRepository.findAll()).thenReturn(List.of(entity));
        when(modelMapper.map(entity, VolunteerProgramResponseDto.class)).thenReturn(dto);

        var out = service.getAllProgramas();

        assertEquals(1, out.size());
        assertEquals(1L, out.get(0).getId());
        verify(programaRepository).findAll();
    }

    // -------- enrollingVolunteerPrograma ----------
    @Test
    public void ShouldThrowAlreadyEnrolledWhenInscriptionExists() {
        Long programaId = 10L, voluntarioId = 5L;
        var programa = new VolunteerProgram();
        programa.setId(programaId);
        programa.setNecessaryVolunteers(10);
        programa.setEnrolledVolunteers(0);
        programa.setStatus(VolunteerProgramStatus.ABIERTO);

        when(programaRepository.findById(programaId)).thenReturn(Optional.of(programa));
        when(inscriptionRepository.existsByVolunteerIdAndVolunteerProgramId(voluntarioId, programaId))
                .thenReturn(true);

        assertThrows(AlreadyEnrolledException.class,
                () -> service.enrollingVolunteerPrograma(programaId, voluntarioId));
    }

    @Test
    public void ShouldMarkAsFullWhenReachCapacity() {
        Long programaId = 10L, voluntarioId = 5L;
        var programa = new VolunteerProgram();
        programa.setId(programaId);
        programa.setNecessaryVolunteers(1);
        programa.setEnrolledVolunteers(0);
        programa.setStatus(VolunteerProgramStatus.ABIERTO);

        var volunteer = new Volunteer();
        volunteer.setId(voluntarioId);

        when(programaRepository.findById(programaId)).thenReturn(Optional.of(programa));
        when(inscriptionRepository.existsByVolunteerIdAndVolunteerProgramId(voluntarioId, programaId))
                .thenReturn(false);

        when(volunteerRepository.findById(voluntarioId)).thenReturn(Optional.of(volunteer));


        when(inscriptionRepository.save(any(Inscription.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        service.enrollingVolunteerPrograma(programaId, voluntarioId);

        assertEquals(1, programa.getEnrolledVolunteers());
        assertEquals(VolunteerProgramStatus.LLENO, programa.getStatus());
        verify(inscriptionRepository).save(any(Inscription.class));
    }

    @Test
    public void ShouldThrowNotFoundWhenProgramDoesNotExist() {
        when(programaRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(VolunteerProgramNotFoundException.class,
                () -> service.getAllVoluntariosInPrograma(999L));
    }
}
