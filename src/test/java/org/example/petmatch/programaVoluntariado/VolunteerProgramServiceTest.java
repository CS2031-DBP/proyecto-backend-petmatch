package org.example.petmatch.programaVoluntariado;

import org.example.petmatch.Shelter.Domain.Shelter;
import org.example.petmatch.Shelter.Exceptions.ShelterNotFoundException;
import org.example.petmatch.Shelter.Infraestructure.ShelterRepository;
import org.example.petmatch.Inscription.domain.Inscription;
import org.example.petmatch.Inscription.exception.AlreadyEnrolledException;
import org.example.petmatch.Inscription.exception.InscriptionNotFoundException;
import org.example.petmatch.Inscription.infrastructure.InscriptionRepository;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramRequestDto;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramService;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramStatus;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.example.petmatch.Volunteer_Program.Infraestructure.VolunteerProgramRepository;
import org.example.petmatch.Volunteer_Program.exception.VolunteerProgramIsFullException;
import org.example.petmatch.Volunteer_Program.exception.VolunteerProgramNotFoundException;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.example.petmatch.Volunteer.Domain.Volunteer;
import org.example.petmatch.Volunteer.Infraestructure.VolunteerRepository;
import org.example.petmatch.Volunteer.dto.VolunteerResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VolunteerProgramServiceTest {
    @Mock
    VolunteerProgramRepository programaRepository;
    @Mock
    ShelterRepository shelterRepository;
    @Mock
    VolunteerRepository volunteerRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    InscriptionRepository inscriptionRepository;
    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    VolunteerProgramService service;

    private VolunteerProgram programa(Long id, String nombre, int necesarios, int inscritos, VolunteerProgramStatus status) {
        var p = new VolunteerProgram();
        p.setId(id);
        p.setName(nombre);
        p.setDescription("desc");
        p.setStartDate(ZonedDateTime.now().minusDays(1));
        p.setFinishDate(ZonedDateTime.now().plusDays(7));
        p.setLocation("Lima");
        p.setNecessaryVolunteers(necesarios);
        p.setEnrolledVolunteers(inscritos);
        p.setStatus(status);
        p.setEnrolled(new ArrayList<>());
        return p;
    }

    private Volunteer voluntario(Long id, String email) {
        var v = new Volunteer();
        v.setId(id);
        v.setEmail(email);
        v.setPassword("x");
        v.setName("Vol");
        v.setLastname("Untario");
        return v;
    }

    private Shelter albergue(Long id, String email, String address) {
        var a = new Shelter();
        a.setId(id);
        a.setEmail(email);
        a.setAddress(address);
        a.setPassword("x");
        return a;
    }

    private Inscription inscripcion(Volunteer v, VolunteerProgram p) {
        if (v.getId() == null) v.setId(999_000L);
        if (p.getId() == null) p.setId(888_000L);

        Inscription ins = new Inscription(v, p);

        v.getInscriptions().add(ins);
        p.getEnrolled().add(ins);

        return ins;
    }

    @Test
    @DisplayName("shouldReturnDtosWhenProgramasExist")
    void shouldReturnDtosWhenProgramasExist() {
        var p1 = programa(1L, "Rescate", 5, 2, VolunteerProgramStatus.ABIERTO);
        var p2 = programa(2L, "Adopciones", 10, 7, VolunteerProgramStatus.EN_PROCESO);
        when(programaRepository.findAll()).thenReturn(List.of(p1, p2));

        var res = service.getAllProgramas();

        assertThat(res).hasSize(2);
        assertThat(res.get(0).getId()).isEqualTo(1L);
        assertThat(res.get(1).getNombre()).isEqualTo("Adopciones");
        verify(programaRepository).findAll();
    }

    @Test
    @DisplayName("shouldReturnEmptyListWhenNoProgramasExist")
    void shouldReturnEmptyListWhenNoProgramasExist() {
        when(programaRepository.findAll()).thenReturn(List.of());

        var res = service.getAllProgramas();

        assertThat(res).isEmpty();
        verify(programaRepository).findAll();
    }

    @Test
    @DisplayName("shouldReturnVoluntariosDtosWhenProgramaExists")
    void shouldReturnVoluntariosDtosWhenProgramaExists() {
        var p = programa(10L, "Esterilización", 3, 0, VolunteerProgramStatus.ABIERTO);
        var v1 = voluntario(100L, "a@x.com");
        var v2 = voluntario(101L, "b@x.com");

        v1.addInscription(p);
        v2.addInscription(p);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));

        var res = service.getAllVoluntariosInPrograma(10L);

        assertThat(res).hasSize(2);
        assertThat(res).extracting(VolunteerResponseDto::getId)
                .containsExactlyInAnyOrder(100L, 101L);
    }

    @Test
    @DisplayName("shouldThrowProgramaNotFoundWhenGettingVoluntariosOfMissingPrograma")
    void shouldThrowProgramaNotFoundWhenGettingVoluntariosOfMissingPrograma() {
        when(programaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(VolunteerProgramNotFoundException.class,
                () -> service.getAllVoluntariosInPrograma(999L));
    }

    @Test
    @DisplayName("shouldCreateProgramaAndReturnNewIdWhenAlbergueEmailExists")
    void shouldCreateProgramaAndReturnNewIdWhenAlbergueEmailExists() {
        var alb = albergue(5L, "alb@x.com", "Av. Siempre Viva 742");
        var req = new VolunteerProgramRequestDto();
        req.setNombre("Vacunatón");
        req.setDescripcion("Campaña");
        req.setFechaInicio(ZonedDateTime.now().plusDays(2));
        req.setFechaFin(ZonedDateTime.now().plusDays(7));
        req.setNumeroVoluntariosNecesarios(10);

        when(shelterRepository.findByEmail("alb@x.com")).thenReturn(Optional.of(alb));
        when(shelterRepository.findById(5L)).thenReturn(Optional.of(alb));
        when(programaRepository.save(any())).thenAnswer(inv -> {
            var in = (VolunteerProgram) inv.getArgument(0);
            in.setId(99L);
            return in;
        });

        var out = service.createPrograma("alb@x.com", req);

        assertThat(out.getId()).isEqualTo(99L);
        verify(programaRepository).save(argThat(p ->
                p.getName().equals("Vacunatón")
                        && p.getShelter().getId().equals(5L)
                        && "Av. Siempre Viva 742".equals(p.getLocation())
        ));
    }

    @Test
    @DisplayName("shouldThrowAlbergueNotFoundWhenCreatingProgramaWithUnknownEmail")
    void shouldThrowAlbergueNotFoundWhenCreatingProgramaWithUnknownEmail() {
        when(shelterRepository.findByEmail("no@x.com")).thenReturn(Optional.empty());
        var req = new VolunteerProgramRequestDto();

        assertThrows(ShelterNotFoundException.class,
                () -> service.createPrograma("no@x.com", req));
    }

    @Test
    @DisplayName("shouldEnrollWhenNotAlreadyEnrolledAndProgramOpen")
    void shouldEnrollWhenNotAlreadyEnrolledAndProgramOpen() {
        var p = programa(10L, "Rescate", 2, 1, VolunteerProgramStatus.ABIERTO); // 1/2
        var v = voluntario(200L, "v@x.com");

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        // Corregido: si NO está inscrito → existsBy... = false
        when(inscriptionRepository.existsByVolunteerIdAndVolunteerProgramId(200L, 10L)).thenReturn(false);
        // Encontrar o crear voluntario: ya existe
        when(volunteerRepository.findById(200L)).thenReturn(Optional.of(v));
        when(inscriptionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.enrollingVolunteerPrograma(10L, 200L);

        // addInscripcion debe haber incrementado a 2/2 y marcado LLENO
        assertThat(p.getEnrolledVolunteers()).isEqualTo(2);
        assertThat(p.getStatus()).isEqualTo(VolunteerProgramStatus.LLENO);
        verify(inscriptionRepository).save(any(Inscription.class));
    }

    @Test
    @DisplayName("shouldThrowAlreadyEnrolledWhenInscripcionExists")
    void shouldThrowAlreadyEnrolledWhenInscripcionExists() {
        var p = programa(10L, "Rescate", 5, 1, VolunteerProgramStatus.ABIERTO);
        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(inscriptionRepository.existsByVolunteerIdAndVolunteerProgramId(200L, 10L))
                .thenReturn(true); // ya inscrito

        assertThrows(AlreadyEnrolledException.class,
                () -> service.enrollingVolunteerPrograma(10L, 200L));
        verify(inscriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("shouldThrowProgramaIsFullWhenProgramaIsAlreadyFull")
    void shouldThrowProgramaIsFullWhenProgramaIsAlreadyFull() {
        var p = programa(10L, "Rescate", 2, 2, VolunteerProgramStatus.LLENO);
        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(inscriptionRepository.existsByVolunteerIdAndVolunteerProgramId(200L, 10L))
                .thenReturn(false);

        assertThrows(VolunteerProgramIsFullException.class,
                () -> service.enrollingVolunteerPrograma(10L, 200L));
    }

    @Test
    @DisplayName("shouldEnrollAndMarkFullWhenReachingCapacity")
    void shouldEnrollAndMarkFullWhenReachingCapacity() {
        // capacidad 2, actualmente 1 -> después de inscribir debe quedar en 2 y LLENO (por addInscripcion)
        var p = programa(10L, "Rescate", 2, 1, VolunteerProgramStatus.ABIERTO);
        var v = voluntario(200L, "v@x.com");

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(inscriptionRepository.existsByVolunteerIdAndVolunteerProgramId(200L, 10L))
                .thenReturn(false);
        when(volunteerRepository.findById(200L)).thenReturn(Optional.of(v));
        when(inscriptionRepository.save(any(Inscription.class))).thenAnswer(inv -> inv.getArgument(0));

        service.enrollingVolunteerPrograma(10L, 200L);

        assertThat(p.getEnrolledVolunteers()).isEqualTo(2);
        assertThat(p.getStatus()).isEqualTo(VolunteerProgramStatus.LLENO);
        verify(inscriptionRepository).save(any(Inscription.class));
    }

    @Test
    @DisplayName("shouldReturnExistingVoluntarioWhenFound")
    void shouldReturnExistingVoluntarioWhenFound() {
        var v = voluntario(300L, "v@x.com");
        when(volunteerRepository.findById(300L)).thenReturn(Optional.of(v));

        var res = service.encontrarOCrearVoluntario(300L);

        assertThat(res.getId()).isEqualTo(300L);
        verify(volunteerRepository, never()).save(any());
    }

    @Test
    @DisplayName("shouldCreateVoluntarioWhenUserExistsButVoluntarioNotFound")
    void shouldCreateVoluntarioWhenUserExistsButVoluntarioNotFound() {
        when(volunteerRepository.findById(300L)).thenReturn(Optional.empty());
        when(userRepository.existsById(300L)).thenReturn(true);
        when(volunteerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Volunteer v = service.encontrarOCrearVoluntario(300L);

        assertThat(v.getId()).isEqualTo(300L);
        verify(volunteerRepository).save(any());
    }

    @Test
    @DisplayName("shouldThrowWhenUserIdNotExists")
    void shouldThrowWhenUserIdNotExists() {
        when(volunteerRepository.findById(400L)).thenReturn(Optional.empty());
        when(userRepository.existsById(400L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.encontrarOCrearVoluntario(400L));
        verify(volunteerRepository, never()).save(any());
    }

    @Test
    @DisplayName("shouldUnenrollWhenInscripcionExists")
    void shouldUnenrollWhenInscripcionExists() {
        var p = programa(10L, "Rescate", 5, 1, VolunteerProgramStatus.EN_PROCESO);
        var v = voluntario(200L, "v@x.com");
        var ins = inscripcion(v, p);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(volunteerRepository.findById(200L)).thenReturn(Optional.of(v));
        when(inscriptionRepository.findByVolunteerIdAndVolunteerProgramId(200L, 10L))
                .thenReturn(Optional.of(ins));

        service.unsubscribeVolunteerProgram(10L, 200L);

        verify(inscriptionRepository).delete(any(Inscription.class));
    }

    @Test
    @DisplayName("shouldThrowWhenUnenrollInscripcionMissing")
    void shouldThrowWhenUnenrollInscripcionMissing() {
        var p = programa(10L, "Rescate", 5, 0, VolunteerProgramStatus.ABIERTO);
        var v = voluntario(200L, "v@x.com");

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(volunteerRepository.findById(200L)).thenReturn(Optional.of(v));
        when(inscriptionRepository.findByVolunteerIdAndVolunteerProgramId(200L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.unsubscribeVolunteerProgram(10L, 200L));
    }

    @Test
    @DisplayName("shouldUnenrollWhenAlbergueOwnsPrograma")
    void shouldUnenrollWhenAlbergueOwnsPrograma() {
        var alb = albergue(5L, "alb@x.com", "Av. 1");
        var p = programa(10L, "Rescate", 5, 1, VolunteerProgramStatus.EN_PROCESO);
        p.setShelter(alb);
        var v = voluntario(200L, "v@x.com");
        var ins = inscripcion(v, p);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(shelterRepository.findByEmail("alb@x.com")).thenReturn(Optional.of(alb));
        when(volunteerRepository.findById(200L)).thenReturn(Optional.of(v));
        when(inscriptionRepository.findByVolunteerIdAndVolunteerProgramId(200L, 10L))
                .thenReturn(Optional.of(ins));

        service.unsubscribeVolunteerProgramOnShelter(10L, 200L, "alb@x.com");

        verify(inscriptionRepository).delete(any(Inscription.class));
    }

    @Test
    @DisplayName("shouldThrowWhenAlbergueNotOwner")
    void shouldThrowWhenAlbergueNotOwner() {
        var owner = albergue(5L, "owner@x.com", "Av. X");
        var other = albergue(7L, "otro@x.com", "Av. Y");
        var p = programa(10L, "Rescate", 5, 1, VolunteerProgramStatus.EN_PROCESO);
        p.setShelter(owner);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(shelterRepository.findByEmail("otro@x.com")).thenReturn(Optional.of(other));

        assertThrows(RuntimeException.class,
                () -> service.unsubscribeVolunteerProgramOnShelter(10L, 200L, "otro@x.com"));
    }

    @Test
    @DisplayName("shouldUnenrollSelfWhenInscripcionExists")
    void shouldUnenrollSelfWhenInscripcionExists() {
        var p = programa(10L, "Rescate", 5, 1, VolunteerProgramStatus.EN_PROCESO);
        var v = voluntario(200L, "user@x.com");
        var ins = inscripcion(v, p);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(volunteerRepository.findByEmail("user@x.com")).thenReturn(Optional.of(v));
        // Corregido: (voluntarioId, programaId)
        when(inscriptionRepository.findByVolunteerIdAndVolunteerProgramId(200L, 10L))
                .thenReturn(Optional.of(ins));

        service.desinscribirDePrograma(10L, "user@x.com");

        verify(volunteerRepository).save(any(Volunteer.class));
    }

    @Test
    @DisplayName("shouldThrowInscripcionNotFoundWhenRepoReturnsEmptyInDesinscribirDePrograma")
    void shouldThrowInscripcionNotFoundWhenRepoReturnsEmptyInDesinscribirDePrograma() {
        var p = programa(10L, "Rescate", 5, 1, VolunteerProgramStatus.EN_PROCESO);
        var v = voluntario(200L, "user@x.com");

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(volunteerRepository.findByEmail("user@x.com")).thenReturn(Optional.of(v));
        when(inscriptionRepository.findByVolunteerIdAndVolunteerProgramId(200L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(InscriptionNotFoundException.class,
                () -> service.desinscribirDePrograma(10L, "user@x.com"));

        verify(volunteerRepository, never()).save(any());
    }

    @Test
    @DisplayName("shouldDeleteWhenProgramaExists")
    void shouldDeleteWhenProgramaExists() {
        when(programaRepository.existsById(10L)).thenReturn(true);

        service.deletePrograma(10L);

        verify(programaRepository).deleteById(10L);
    }

    @Test
    @DisplayName("shouldThrowWhenDeletingNonExistingPrograma")
    void shouldThrowWhenDeletingNonExistingPrograma() {
        when(programaRepository.existsById(10L)).thenReturn(false);

        assertThrows(VolunteerProgramNotFoundException.class, () -> service.deletePrograma(10L));
        verify(programaRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("shouldDeleteWhenAlbergueCreatorMatches")
    void shouldDeleteWhenAlbergueCreatorMatches() {
        var alb = albergue(5L, "alb@x.com", "Av.");
        var p = programa(10L, "Rescate", 5, 1, VolunteerProgramStatus.EN_PROCESO);
        p.setShelter(alb);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(shelterRepository.findByEmail("alb@x.com")).thenReturn(Optional.of(alb));

        service.albergueDeletePrograma(10L, "alb@x.com");

        verify(programaRepository).deleteById(10L);
    }

    @Test
    @DisplayName("shouldThrowWhenAlbergueCreatorMismatch")
    void shouldThrowWhenAlbergueCreatorMismatch() {
        var owner = albergue(5L, "owner@x.com", "Av");
        var other = albergue(7L, "otro@x.com", "Av");
        var p = programa(10L, "Rescate", 5, 1, VolunteerProgramStatus.EN_PROCESO);
        p.setShelter(owner);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(shelterRepository.findByEmail("otro@x.com")).thenReturn(Optional.of(other));

        assertThrows(RuntimeException.class,
                () -> service.albergueDeletePrograma(10L, "otro@x.com"));

        verify(programaRepository, never()).deleteById(anyLong());
    }

}
