package org.example.petmatch.voluntario;

import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramResponseDto;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramStatus;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.example.petmatch.Volunteer.Domain.Volunteer;
import org.example.petmatch.Volunteer.Domain.VolunteerService;
import org.example.petmatch.Volunteer.Infraestructure.VolunteerRepository;
import org.example.petmatch.Volunteer.dto.VolunteerResponseDto;
import org.example.petmatch.Volunteer.exception.VolunteerNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VolunteerServiceTest {
    @Mock
    VolunteerRepository volunteerRepository;

    @Spy
    ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    VolunteerService service;

    private Volunteer voluntario(Long id, String nombre, String apellido, String email) {
        Volunteer v = new Volunteer();
        v.setId(id);
        v.setName(nombre);
        v.setLastname(apellido);
        v.setEmail(email);
        v.setPassword("secret");
        return v;
    }

    private VolunteerProgram programa(Long id, String nombre) {
        VolunteerProgram p = new VolunteerProgram();
        p.setId(id);
        p.setNombre(nombre);
        p.setDescripcion("desc " + nombre);
        p.setFechaInicio(ZonedDateTime.now().minusDays(10));
        p.setFechaFin(ZonedDateTime.now().plusDays(10));
        p.setUbicacion("Lima");
        p.setNumeroVoluntariosNecesarios(5);
        p.setNumeroVoluntariosInscritos(0);
        p.setStatus(VolunteerProgramStatus.ABIERTO); // o el que corresponda
        return p;
    }

    @Test
    @DisplayName("shouldReturnDtosWhenVoluntariosExist")
    void shouldReturnDtosWhenVoluntariosExist() {

        Volunteer v1 = voluntario(1L, "Ana", "Lopez", "ana@x.com");
        Volunteer v2 = voluntario(2L, "Bruno", "Diaz", "bruno@x.com");
        when(volunteerRepository.findAll()).thenReturn(List.of(v1, v2));


        List<VolunteerResponseDto> dtos = service.getAllVoluntarios();


        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(0).getName()).isEqualTo("Ana");
        assertThat(dtos.get(1).getLastname()).isEqualTo("Diaz");
        verify(volunteerRepository).findAll();
    }

    @Test
    @DisplayName("shouldReturnEmptyListWhenNoVoluntariosExist")
    void shouldReturnEmptyListWhenNoVoluntariosExist() {

        when(volunteerRepository.findAll()).thenReturn(List.of());


        List<VolunteerResponseDto> dtos = service.getAllVoluntarios();


        assertThat(dtos).isEmpty();
        verify(volunteerRepository).findAll();
    }

    @Test
    @DisplayName("shouldReturnProgramasWhenVoluntarioExists")
    void shouldReturnProgramasWhenVoluntarioExists() {

        Volunteer vol = voluntario(33L, "Carla", "Perez", "carla@x.com");
        VolunteerProgram p1 = programa(100L, "Rescate");
        VolunteerProgram p2 = programa(101L, "Adopciones");

        vol.addInscripcion(p1);
        vol.addInscripcion(p2);
        when(volunteerRepository.findById(33L)).thenReturn(Optional.of(vol));


        List<VolunteerProgramResponseDto> dtos = service.getProgramaByVoluntarioId(33L);


        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(VolunteerProgramResponseDto::getId)
                .containsExactlyInAnyOrder(100L, 101L);
        assertThat(dtos).extracting(VolunteerProgramResponseDto::getNombre)
                .contains("Rescate", "Adopciones");
        verify(volunteerRepository).findById(33L);
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenVoluntarioNotFound")
    void shouldThrowExceptionWhenVoluntarioNotFound() {

        when(volunteerRepository.findById(999L)).thenReturn(Optional.empty());


        assertThrows(VolunteerNotFoundException.class,
                () -> service.getProgramaByVoluntarioId(999L));

        verify(volunteerRepository).findById(999L);
    }




}
