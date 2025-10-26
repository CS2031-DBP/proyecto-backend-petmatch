package org.example.petmatch.programaVoluntariado;

import org.example.petmatch.Albergue.Domain.Albergue;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaStatus;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaVoluntariado;
import org.example.petmatch.Programa_voluntariado.Infraestructure.ProgramaVoluntariadoRepository;
import org.example.petmatch.Voluntario.Domain.Voluntario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProgramaRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    TestEntityManager em;

    @Autowired
    ProgramaVoluntariadoRepository programaRepo;

    private Albergue albergue(String email, String address) {
        Albergue a = new Albergue();
        a.setEmail(email);
        a.setPassword("x");
        a.setAddress(address);
        return a;
    }

    private ProgramaVoluntariado programa(String nombre, int necesarios, ProgramaStatus status) {
        ProgramaVoluntariado p = new ProgramaVoluntariado();
        p.setNombre(nombre);
        p.setDescripcion("desc " + nombre);
        p.setFechaInicio(ZonedDateTime.now().minusDays(2));
        p.setFechaFin(ZonedDateTime.now().plusDays(5));
        p.setUbicacion("Lima");
        p.setNumeroVoluntariosNecesarios(necesarios);
        p.setNumeroVoluntariosInscritos(0);
        p.setStatus(status);
        return p;
    }

    private Voluntario voluntario(String email) {
        Voluntario v = new Voluntario();
        v.setEmail(email);
        v.setPassword("x");
        v.setName("Vol");
        v.setLastname("Untario");
        return v;
    }

    private void inscribir(Voluntario v, ProgramaVoluntariado p) {
        v.addInscripcion(p);
        em.persist(v);
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("shouldPersistAndLoadProgramaWhenSaved")
    void shouldPersistAndLoadProgramaWhenSaved() {
        var p = programa("Rescate", 5, ProgramaStatus.ABIERTO);
        p = em.persistFlushFind(p);

        Optional<ProgramaVoluntariado> found = programaRepo.findById(p.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Rescate");
        assertThat(found.get().getNumeroVoluntariosInscritos()).isZero();
    }

    @Test
    @DisplayName("shouldReturnEmptyWhenIdDoesNotExist")
    void shouldReturnEmptyWhenIdDoesNotExist() {
        assertThat(programaRepo.findById(123456L)).isEmpty();
    }

    @Test
    @DisplayName("shouldAssociateAlbergueWhenSaved")
    void shouldAssociateAlbergueWhenSaved() {
        var alb = albergue("alb@x.com", "Av. Siempre Viva 742");
        alb = em.persistFlushFind(alb);

        var p = programa("Adopciones", 10, ProgramaStatus.ABIERTO);
        p.setAlbergue(alb);
        p = em.persistFlushFind(p);

        var again = programaRepo.findById(p.getId()).orElseThrow();
        assertThat(again.getAlbergue().getEmail()).isEqualTo("alb@x.com");
        assertThat(again.getUbicacion()).isEqualTo("Lima");
    }

    @Test
    @DisplayName("shouldReflectVoluntariosViaInscripciones")
    void shouldReflectVoluntariosViaInscripciones() {
        var p = em.persistFlushFind(programa("Esterilización", 3, ProgramaStatus.ABIERTO));
        var v1 = em.persistFlushFind(voluntario("a@x.com"));
        var v2 = em.persistFlushFind(voluntario("b@x.com"));

        inscribir(v1, p);
        inscribir(v2, p);

        var reloaded = programaRepo.findById(p.getId()).orElseThrow();

        assertThat(reloaded.getVoluntarios()).extracting(Voluntario::getEmail)
                .containsExactlyInAnyOrder("a@x.com", "b@x.com");
        assertThat(reloaded.getNumeroVoluntariosInscritos()).isEqualTo(2);
    }

    @Test
    @DisplayName("shouldMarkFullWhenInscritosReachNecesarios")
    void shouldMarkFullWhenInscritosReachNecesarios() {
        var p = em.persistFlushFind(programa("Vacunatón", 2, ProgramaStatus.ABIERTO));
        var v1 = em.persistFlushFind(voluntario("v1@x.com"));
        var v2 = em.persistFlushFind(voluntario("v2@x.com"));

        inscribir(v1, p);
        inscribir(v2, p);

        var reloaded = programaRepo.findById(p.getId()).orElseThrow();
        assertThat(reloaded.getNumeroVoluntariosInscritos()).isEqualTo(2);
        assertThat(reloaded.getStatus()).isEqualTo(ProgramaStatus.LLENO);
        assertThat(reloaded.isLleno()).isTrue();
    }

    @Test
    @DisplayName("shouldRemoveInscripcionAndUpdateCountersWhenVoluntarioRemoves")
    void shouldRemoveInscripcionAndUpdateCountersWhenVoluntarioRemoves() {
        var p = em.persistFlushFind(programa("Campaña", 2, ProgramaStatus.ABIERTO));
        var v1 = em.persistFlushFind(voluntario("v1@x.com"));
        var v2 = em.persistFlushFind(voluntario("v2@x.com"));

        inscribir(v1, p);
        inscribir(v2, p);


        var managedVol = em.find(Voluntario.class, v2.getId());
        var managedProg = em.find(ProgramaVoluntariado.class, p.getId());
        managedVol.removeInscripcion(managedProg);
        em.flush();
        em.clear();

        var reloaded = programaRepo.findById(p.getId()).orElseThrow();
        assertThat(reloaded.getNumeroVoluntariosInscritos()).isEqualTo(1);
        assertThat(reloaded.getStatus()).isEqualTo(ProgramaStatus.ABIERTO);
        assertThat(reloaded.getVoluntarios()).extracting(Voluntario::getEmail)
                .containsExactly("v1@x.com");
    }

    @Test
    @DisplayName("shouldExistByIdWhenSavedAndShouldDeleteById")
    void shouldExistByIdWhenSavedAndShouldDeleteById() {
        var p = em.persistFlushFind(programa("Esterilización masiva", 5, ProgramaStatus.ABIERTO));

        assertThat(programaRepo.existsById(p.getId())).isTrue();

        programaRepo.deleteById(p.getId());
        em.flush();

        assertThat(programaRepo.findById(p.getId())).isEmpty();
        assertThat(programaRepo.existsById(p.getId())).isFalse();
    }
}
