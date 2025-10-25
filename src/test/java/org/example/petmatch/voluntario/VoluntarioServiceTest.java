package org.example.petmatch.voluntario;

import org.example.petmatch.Programa_voluntariado.DTO.ProgramaResponseDto;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaStatus;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaVoluntariado;
import org.example.petmatch.Voluntario.Domain.Voluntario;
import org.example.petmatch.Voluntario.Domain.VoluntarioService;
import org.example.petmatch.Voluntario.Infraestructure.VoluntarioRepository;
import org.example.petmatch.Voluntario.dto.VoluntarioResponseDto;
import org.example.petmatch.Voluntario.exception.VoluntarioNotFoundException;
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
public class VoluntarioServiceTest {
    @Mock
    VoluntarioRepository voluntarioRepository;

    @Spy
    ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    VoluntarioService service;

    private Voluntario voluntario(Long id, String nombre, String apellido, String email) {
        Voluntario v = new Voluntario();
        v.setId(id);
        v.setName(nombre);
        v.setLastname(apellido);
        v.setEmail(email);
        v.setPassword("secret");
        return v;
    }

    private ProgramaVoluntariado programa(Long id, String nombre) {
        ProgramaVoluntariado p = new ProgramaVoluntariado();
        p.setId(id);
        p.setNombre(nombre);
        p.setDescripcion("desc " + nombre);
        p.setFechaInicio(ZonedDateTime.now().minusDays(10));
        p.setFechaFin(ZonedDateTime.now().plusDays(10));
        p.setUbicacion("Lima");
        p.setNumeroVoluntariosNecesarios(5);
        p.setNumeroVoluntariosInscritos(0);
        p.setStatus(ProgramaStatus.ABIERTO); // o el que corresponda
        return p;
    }

    @Test
    @DisplayName("shouldReturnDtosWhenVoluntariosExist")
    void shouldReturnDtosWhenVoluntariosExist() {

        Voluntario v1 = voluntario(1L, "Ana", "Lopez", "ana@x.com");
        Voluntario v2 = voluntario(2L, "Bruno", "Diaz", "bruno@x.com");
        when(voluntarioRepository.findAll()).thenReturn(List.of(v1, v2));


        List<VoluntarioResponseDto> dtos = service.getAllVoluntarios();


        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(0).getName()).isEqualTo("Ana");
        assertThat(dtos.get(1).getLastname()).isEqualTo("Diaz");
        verify(voluntarioRepository).findAll();
    }

    @Test
    @DisplayName("shouldReturnEmptyListWhenNoVoluntariosExist")
    void shouldReturnEmptyListWhenNoVoluntariosExist() {

        when(voluntarioRepository.findAll()).thenReturn(List.of());


        List<VoluntarioResponseDto> dtos = service.getAllVoluntarios();


        assertThat(dtos).isEmpty();
        verify(voluntarioRepository).findAll();
    }

    @Test
    @DisplayName("shouldReturnProgramasWhenVoluntarioExists")
    void shouldReturnProgramasWhenVoluntarioExists() {

        Voluntario vol = voluntario(33L, "Carla", "Perez", "carla@x.com");
        ProgramaVoluntariado p1 = programa(100L, "Rescate");
        ProgramaVoluntariado p2 = programa(101L, "Adopciones");

        vol.addInscripcion(p1);
        vol.addInscripcion(p2);
        when(voluntarioRepository.findById(33L)).thenReturn(Optional.of(vol));


        List<ProgramaResponseDto> dtos = service.getProgramaByVoluntarioId(33L);


        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(ProgramaResponseDto::getId)
                .containsExactlyInAnyOrder(100L, 101L);
        assertThat(dtos).extracting(ProgramaResponseDto::getNombre)
                .contains("Rescate", "Adopciones");
        verify(voluntarioRepository).findById(33L);
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenVoluntarioNotFound")
    void shouldThrowExceptionWhenVoluntarioNotFound() {

        when(voluntarioRepository.findById(999L)).thenReturn(Optional.empty());


        assertThrows(VoluntarioNotFoundException.class,
                () -> service.getProgramaByVoluntarioId(999L));

        verify(voluntarioRepository).findById(999L);
    }




}
