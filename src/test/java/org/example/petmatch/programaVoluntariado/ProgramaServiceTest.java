package org.example.petmatch.programaVoluntariado;

import org.example.petmatch.Albergue.Domain.Albergue;
import org.example.petmatch.Albergue.Exceptions.AlbergueNotFoundException;
import org.example.petmatch.Albergue.Infraestructure.AlbergueRepository;
import org.example.petmatch.Inscripcion.domain.Inscripcion;
import org.example.petmatch.Inscripcion.exception.AlreadyEnrolledException;
import org.example.petmatch.Inscripcion.exception.InscripcionNotFoundException;
import org.example.petmatch.Inscripcion.infrastructure.InscripcionRepository;
import org.example.petmatch.Programa_voluntariado.DTO.ProgramaRequestDto;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaService;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaStatus;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaVoluntariado;
import org.example.petmatch.Programa_voluntariado.Infraestructure.ProgramaVoluntariadoRepository;
import org.example.petmatch.Programa_voluntariado.exception.ProgramaIsFullException;
import org.example.petmatch.Programa_voluntariado.exception.ProgramaNotFoundException;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.example.petmatch.Voluntario.Domain.Voluntario;
import org.example.petmatch.Voluntario.Infraestructure.VoluntarioRepository;
import org.example.petmatch.Voluntario.dto.VoluntarioResponseDto;
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
public class ProgramaServiceTest {
    @Mock
    ProgramaVoluntariadoRepository programaRepository;
    @Mock
    AlbergueRepository albergueRepository;
    @Mock
    VoluntarioRepository voluntarioRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    InscripcionRepository inscripcionRepository;
    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    ProgramaService service;

    private ProgramaVoluntariado programa(Long id, String nombre, int necesarios, int inscritos, ProgramaStatus status) {
        var p = new ProgramaVoluntariado();
        p.setId(id);
        p.setNombre(nombre);
        p.setDescripcion("desc");
        p.setFechaInicio(ZonedDateTime.now().minusDays(1));
        p.setFechaFin(ZonedDateTime.now().plusDays(7));
        p.setUbicacion("Lima");
        p.setNumeroVoluntariosNecesarios(necesarios);
        p.setNumeroVoluntariosInscritos(inscritos);
        p.setStatus(status);
        p.setInscritos(new ArrayList<>());
        return p;
    }

    private Voluntario voluntario(Long id, String email) {
        var v = new Voluntario();
        v.setId(id);
        v.setEmail(email);
        v.setPassword("x");
        v.setName("Vol");
        v.setLastname("Untario");
        return v;
    }

    private Albergue albergue(Long id, String email, String address) {
        var a = new Albergue();
        a.setId(id);
        a.setEmail(email);
        a.setAddress(address);
        a.setPassword("x");
        return a;
    }

    private Inscripcion inscripcion(Voluntario v, ProgramaVoluntariado p) {
        if (v.getId() == null) v.setId(999_000L);
        if (p.getId() == null) p.setId(888_000L);

        Inscripcion ins = new Inscripcion(v, p);

        v.getInscripciones().add(ins);
        p.getInscritos().add(ins);

        return ins;
    }

    @Test
    @DisplayName("shouldReturnDtosWhenProgramasExist")
    void shouldReturnDtosWhenProgramasExist() {
        var p1 = programa(1L, "Rescate", 5, 2, ProgramaStatus.ABIERTO);
        var p2 = programa(2L, "Adopciones", 10, 7, ProgramaStatus.EN_PROCESO);
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
        var p = programa(10L, "Esterilización", 3, 0, ProgramaStatus.ABIERTO);
        var v1 = voluntario(100L, "a@x.com");
        var v2 = voluntario(101L, "b@x.com");

        v1.addInscripcion(p);
        v2.addInscripcion(p);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));

        var res = service.getAllVoluntariosInPrograma(10L);

        assertThat(res).hasSize(2);
        assertThat(res).extracting(VoluntarioResponseDto::getId)
                .containsExactlyInAnyOrder(100L, 101L);
    }

    @Test
    @DisplayName("shouldThrowProgramaNotFoundWhenGettingVoluntariosOfMissingPrograma")
    void shouldThrowProgramaNotFoundWhenGettingVoluntariosOfMissingPrograma() {
        when(programaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProgramaNotFoundException.class,
                () -> service.getAllVoluntariosInPrograma(999L));
    }

    @Test
    @DisplayName("shouldCreateProgramaAndReturnNewIdWhenAlbergueEmailExists")
    void shouldCreateProgramaAndReturnNewIdWhenAlbergueEmailExists() {
        var alb = albergue(5L, "alb@x.com", "Av. Siempre Viva 742");
        var req = new ProgramaRequestDto();
        req.setNombre("Vacunatón");
        req.setDescripcion("Campaña");
        req.setFechaInicio(ZonedDateTime.now().plusDays(2));
        req.setFechaFin(ZonedDateTime.now().plusDays(7));
        req.setNumeroVoluntariosNecesarios(10);

        when(albergueRepository.findByEmail("alb@x.com")).thenReturn(Optional.of(alb));
        when(albergueRepository.findById(5L)).thenReturn(Optional.of(alb));
        when(programaRepository.save(any())).thenAnswer(inv -> {
            var in = (ProgramaVoluntariado) inv.getArgument(0);
            in.setId(99L);
            return in;
        });

        var out = service.createPrograma("alb@x.com", req);

        assertThat(out.getId()).isEqualTo(99L);
        verify(programaRepository).save(argThat(p ->
                p.getNombre().equals("Vacunatón")
                        && p.getAlbergue().getId().equals(5L)
                        && "Av. Siempre Viva 742".equals(p.getUbicacion())
        ));
    }

    @Test
    @DisplayName("shouldThrowAlbergueNotFoundWhenCreatingProgramaWithUnknownEmail")
    void shouldThrowAlbergueNotFoundWhenCreatingProgramaWithUnknownEmail() {
        when(albergueRepository.findByEmail("no@x.com")).thenReturn(Optional.empty());
        var req = new ProgramaRequestDto();

        assertThrows(AlbergueNotFoundException.class,
                () -> service.createPrograma("no@x.com", req));
    }

    @Test
    @DisplayName("shouldEnrollWhenNotAlreadyEnrolledAndProgramOpen")
    void shouldEnrollWhenNotAlreadyEnrolledAndProgramOpen() {
        var p = programa(10L, "Rescate", 2, 1, ProgramaStatus.ABIERTO); // 1/2
        var v = voluntario(200L, "v@x.com");

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        // Corregido: si NO está inscrito → existsBy... = false
        when(inscripcionRepository.existsByVoluntarioIdAndProgramaVoluntariadoId(200L, 10L)).thenReturn(false);
        // Encontrar o crear voluntario: ya existe
        when(voluntarioRepository.findById(200L)).thenReturn(Optional.of(v));
        when(inscripcionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.inscribirVoluntarioAPrograma(10L, 200L);

        // addInscripcion debe haber incrementado a 2/2 y marcado LLENO
        assertThat(p.getNumeroVoluntariosInscritos()).isEqualTo(2);
        assertThat(p.getStatus()).isEqualTo(ProgramaStatus.LLENO);
        verify(inscripcionRepository).save(any(Inscripcion.class));
    }

    @Test
    @DisplayName("shouldThrowAlreadyEnrolledWhenInscripcionExists")
    void shouldThrowAlreadyEnrolledWhenInscripcionExists() {
        var p = programa(10L, "Rescate", 5, 1, ProgramaStatus.ABIERTO);
        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(inscripcionRepository.existsByVoluntarioIdAndProgramaVoluntariadoId(200L, 10L))
                .thenReturn(true); // ya inscrito

        assertThrows(AlreadyEnrolledException.class,
                () -> service.inscribirVoluntarioAPrograma(10L, 200L));
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    @DisplayName("shouldThrowProgramaIsFullWhenProgramaIsAlreadyFull")
    void shouldThrowProgramaIsFullWhenProgramaIsAlreadyFull() {
        var p = programa(10L, "Rescate", 2, 2, ProgramaStatus.LLENO);
        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(inscripcionRepository.existsByVoluntarioIdAndProgramaVoluntariadoId(200L, 10L))
                .thenReturn(false);

        assertThrows(ProgramaIsFullException.class,
                () -> service.inscribirVoluntarioAPrograma(10L, 200L));
    }

    @Test
    @DisplayName("shouldEnrollAndMarkFullWhenReachingCapacity")
    void shouldEnrollAndMarkFullWhenReachingCapacity() {
        // capacidad 2, actualmente 1 -> después de inscribir debe quedar en 2 y LLENO (por addInscripcion)
        var p = programa(10L, "Rescate", 2, 1, ProgramaStatus.ABIERTO);
        var v = voluntario(200L, "v@x.com");

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(inscripcionRepository.existsByVoluntarioIdAndProgramaVoluntariadoId(200L, 10L))
                .thenReturn(false);
        when(voluntarioRepository.findById(200L)).thenReturn(Optional.of(v));
        when(inscripcionRepository.save(any(Inscripcion.class))).thenAnswer(inv -> inv.getArgument(0));

        service.inscribirVoluntarioAPrograma(10L, 200L);

        assertThat(p.getNumeroVoluntariosInscritos()).isEqualTo(2);
        assertThat(p.getStatus()).isEqualTo(ProgramaStatus.LLENO);
        verify(inscripcionRepository).save(any(Inscripcion.class));
    }

    @Test
    @DisplayName("shouldReturnExistingVoluntarioWhenFound")
    void shouldReturnExistingVoluntarioWhenFound() {
        var v = voluntario(300L, "v@x.com");
        when(voluntarioRepository.findById(300L)).thenReturn(Optional.of(v));

        var res = service.encontrarOCrearVoluntario(300L);

        assertThat(res.getId()).isEqualTo(300L);
        verify(voluntarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("shouldCreateVoluntarioWhenUserExistsButVoluntarioNotFound")
    void shouldCreateVoluntarioWhenUserExistsButVoluntarioNotFound() {
        when(voluntarioRepository.findById(300L)).thenReturn(Optional.empty());
        when(userRepository.existsById(300L)).thenReturn(true);
        when(voluntarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Voluntario v = service.encontrarOCrearVoluntario(300L);

        assertThat(v.getId()).isEqualTo(300L);
        verify(voluntarioRepository).save(any());
    }

    @Test
    @DisplayName("shouldThrowWhenUserIdNotExists")
    void shouldThrowWhenUserIdNotExists() {
        when(voluntarioRepository.findById(400L)).thenReturn(Optional.empty());
        when(userRepository.existsById(400L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.encontrarOCrearVoluntario(400L));
        verify(voluntarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("shouldUnenrollWhenInscripcionExists")
    void shouldUnenrollWhenInscripcionExists() {
        var p = programa(10L, "Rescate", 5, 1, ProgramaStatus.EN_PROCESO);
        var v = voluntario(200L, "v@x.com");
        var ins = inscripcion(v, p);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(voluntarioRepository.findById(200L)).thenReturn(Optional.of(v));
        when(inscripcionRepository.findByVoluntarioIdAndProgramaVoluntariadoId(200L, 10L))
                .thenReturn(Optional.of(ins));

        service.desinscribirVoluntarioDePrograma(10L, 200L);

        verify(inscripcionRepository).delete(any(Inscripcion.class));
    }

    @Test
    @DisplayName("shouldThrowWhenUnenrollInscripcionMissing")
    void shouldThrowWhenUnenrollInscripcionMissing() {
        var p = programa(10L, "Rescate", 5, 0, ProgramaStatus.ABIERTO);
        var v = voluntario(200L, "v@x.com");

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(voluntarioRepository.findById(200L)).thenReturn(Optional.of(v));
        when(inscripcionRepository.findByVoluntarioIdAndProgramaVoluntariadoId(200L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.desinscribirVoluntarioDePrograma(10L, 200L));
    }

    @Test
    @DisplayName("shouldUnenrollWhenAlbergueOwnsPrograma")
    void shouldUnenrollWhenAlbergueOwnsPrograma() {
        var alb = albergue(5L, "alb@x.com", "Av. 1");
        var p = programa(10L, "Rescate", 5, 1, ProgramaStatus.EN_PROCESO);
        p.setAlbergue(alb);
        var v = voluntario(200L, "v@x.com");
        var ins = inscripcion(v, p);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(albergueRepository.findByEmail("alb@x.com")).thenReturn(Optional.of(alb));
        when(voluntarioRepository.findById(200L)).thenReturn(Optional.of(v));
        when(inscripcionRepository.findByVoluntarioIdAndProgramaVoluntariadoId(200L, 10L))
                .thenReturn(Optional.of(ins));

        service.desinscribirVoluntarioDeProgramaAlbergue(10L, 200L, "alb@x.com");

        verify(inscripcionRepository).delete(any(Inscripcion.class));
    }

    @Test
    @DisplayName("shouldThrowWhenAlbergueNotOwner")
    void shouldThrowWhenAlbergueNotOwner() {
        var owner = albergue(5L, "owner@x.com", "Av. X");
        var other = albergue(7L, "otro@x.com", "Av. Y");
        var p = programa(10L, "Rescate", 5, 1, ProgramaStatus.EN_PROCESO);
        p.setAlbergue(owner);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(albergueRepository.findByEmail("otro@x.com")).thenReturn(Optional.of(other));

        assertThrows(RuntimeException.class,
                () -> service.desinscribirVoluntarioDeProgramaAlbergue(10L, 200L, "otro@x.com"));
    }

    @Test
    @DisplayName("shouldUnenrollSelfWhenInscripcionExists")
    void shouldUnenrollSelfWhenInscripcionExists() {
        var p = programa(10L, "Rescate", 5, 1, ProgramaStatus.EN_PROCESO);
        var v = voluntario(200L, "user@x.com");
        var ins = inscripcion(v, p);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(voluntarioRepository.findByEmail("user@x.com")).thenReturn(Optional.of(v));
        // Corregido: (voluntarioId, programaId)
        when(inscripcionRepository.findByVoluntarioIdAndProgramaVoluntariadoId(200L, 10L))
                .thenReturn(Optional.of(ins));

        service.desinscribirDePrograma(10L, "user@x.com");

        verify(voluntarioRepository).save(any(Voluntario.class));
    }

    @Test
    @DisplayName("shouldThrowInscripcionNotFoundWhenRepoReturnsEmptyInDesinscribirDePrograma")
    void shouldThrowInscripcionNotFoundWhenRepoReturnsEmptyInDesinscribirDePrograma() {
        var p = programa(10L, "Rescate", 5, 1, ProgramaStatus.EN_PROCESO);
        var v = voluntario(200L, "user@x.com");

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(voluntarioRepository.findByEmail("user@x.com")).thenReturn(Optional.of(v));
        when(inscripcionRepository.findByVoluntarioIdAndProgramaVoluntariadoId(200L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(InscripcionNotFoundException.class,
                () -> service.desinscribirDePrograma(10L, "user@x.com"));

        verify(voluntarioRepository, never()).save(any());
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

        assertThrows(ProgramaNotFoundException.class, () -> service.deletePrograma(10L));
        verify(programaRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("shouldDeleteWhenAlbergueCreatorMatches")
    void shouldDeleteWhenAlbergueCreatorMatches() {
        var alb = albergue(5L, "alb@x.com", "Av.");
        var p = programa(10L, "Rescate", 5, 1, ProgramaStatus.EN_PROCESO);
        p.setAlbergue(alb);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(albergueRepository.findByEmail("alb@x.com")).thenReturn(Optional.of(alb));

        service.albergueDeletePrograma(10L, "alb@x.com");

        verify(programaRepository).deleteById(10L);
    }

    @Test
    @DisplayName("shouldThrowWhenAlbergueCreatorMismatch")
    void shouldThrowWhenAlbergueCreatorMismatch() {
        var owner = albergue(5L, "owner@x.com", "Av");
        var other = albergue(7L, "otro@x.com", "Av");
        var p = programa(10L, "Rescate", 5, 1, ProgramaStatus.EN_PROCESO);
        p.setAlbergue(owner);

        when(programaRepository.findById(10L)).thenReturn(Optional.of(p));
        when(albergueRepository.findByEmail("otro@x.com")).thenReturn(Optional.of(other));

        assertThrows(RuntimeException.class,
                () -> service.albergueDeletePrograma(10L, "otro@x.com"));

        verify(programaRepository, never()).deleteById(anyLong());
    }

}
