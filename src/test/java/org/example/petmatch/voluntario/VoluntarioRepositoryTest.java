package org.example.petmatch.voluntario;

import org.example.petmatch.Programa_voluntariado.Domain.ProgramaStatus;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaVoluntariado;
import org.example.petmatch.User.Domain.Role;
import org.example.petmatch.Voluntario.Domain.Voluntario;
import org.example.petmatch.Voluntario.Infraestructure.VoluntarioRepository;
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
public class VoluntarioRepositoryTest {

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
    VoluntarioRepository voluntarioRepository;

    private Voluntario voluntario(String name, String lastname, String email) {
        Voluntario v = new Voluntario();
        v.setName(name);
        v.setLastname(lastname);
        v.setEmail(email);
        v.setPassword("secret");
        v.setRole(Role.USER);
        return v;
    }

    private ProgramaVoluntariado programa(String nombre) {
        ProgramaVoluntariado p = new ProgramaVoluntariado();
        p.setNombre(nombre);
        p.setDescripcion("desc " + nombre);
        p.setFechaInicio(ZonedDateTime.now().minusDays(5));
        p.setFechaFin(ZonedDateTime.now().plusDays(5));
        p.setUbicacion("Lima");
        p.setNumeroVoluntariosNecesarios(10);
        p.setNumeroVoluntariosInscritos(0);
        p.setStatus(ProgramaStatus.ABIERTO); // ajusta según tu enum
        return p;
    }

    @Test
    @DisplayName("shouldFindVoluntarioWhenIdExists")
    void shouldFindVoluntarioWhenIdExists() {
        var v = voluntario("Ana", "Lopez", "ana@test.com");
        v = em.persistFlushFind(v);

        Optional<Voluntario> found = voluntarioRepository.findById(v.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("ana@test.com");
    }

    @Test
    @DisplayName("shouldReturnEmptyWhenIdDoesNotExist")
    void shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<Voluntario> notFound = voluntarioRepository.findById(123456L);
        assertThat(notFound).isEmpty();
    }

    @Test
    @DisplayName("shouldFindVoluntarioWhenEmailExists")
    void shouldFindVoluntarioWhenEmailExists() {
        var v = voluntario("Bruno", "Diaz", "bruno@test.com");
        em.persistAndFlush(v);

        Optional<Voluntario> found = voluntarioRepository.findByEmail("bruno@test.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Bruno");
    }

    @Test
    @DisplayName("shouldReturnEmptyWhenEmailDoesNotExist")
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<Voluntario> notFound = voluntarioRepository.findByEmail("noexiste@test.com");
        assertThat(notFound).isEmpty();
    }

    @Test
    @DisplayName("shouldCascadePersistInscripcionesWhenSavingVoluntario")
    void shouldCascadePersistInscripcionesWhenSavingVoluntario() {
        var v = voluntario("Carla", "Perez", "carla@test.com");
        var p1 = programa("Rescate");
        var p2 = programa("Adopciones");


        p1 = em.persistFlushFind(p1);
        p2 = em.persistFlushFind(p2);


        v.addInscripcion(p1);
        v.addInscripcion(p2);

        v = em.persistFlushFind(v);


        assertThat(v.getInscripciones()).hasSize(2);
        assertThat(v.getProgramas()).hasSize(2);
        assertThat(v.getProgramas())
                .extracting(ProgramaVoluntariado::getNombre)
                .containsExactlyInAnyOrder("Rescate", "Adopciones");
    }

    @Test
    @DisplayName("shouldRemoveInscripcionWhenOrphanRemovalIsTrue")
    void shouldRemoveInscripcionWhenOrphanRemovalIsTrue() {
        var v = voluntario("Diego", "Suarez", "diego@test.com");
        var p = programa("Esterilizacion");
        p = em.persistFlushFind(p);

        v.addInscripcion(p);
        v = em.persistFlushFind(v);


        assertThat(v.getInscripciones()).hasSize(1);


        v.removeInscripcion(p);
        v = em.persistFlushFind(v);

        assertThat(v.getInscripciones()).isEmpty();
        assertThat(v.getProgramas()).isEmpty();

    }

}
