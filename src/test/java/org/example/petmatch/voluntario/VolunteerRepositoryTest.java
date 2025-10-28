package org.example.petmatch.voluntario;

import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramStatus;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.example.petmatch.User.Domain.Role;
import org.example.petmatch.Volunteer.Domain.Volunteer;
import org.example.petmatch.Volunteer.Infraestructure.VolunteerRepository;
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
public class VolunteerRepositoryTest {

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
    VolunteerRepository volunteerRepository;

    private Volunteer voluntario(String name, String lastname, String email) {
        Volunteer v = new Volunteer();
        v.setName(name);
        v.setLastname(lastname);
        v.setEmail(email);
        v.setPassword("secret");
        v.setRole(Role.USER);
        return v;
    }

    private VolunteerProgram programa(String nombre) {
        VolunteerProgram p = new VolunteerProgram();
        p.setName(nombre);
        p.setDescription("desc " + nombre);
        p.setStartDate(ZonedDateTime.now().minusDays(5));
        p.setFinishDate(ZonedDateTime.now().plusDays(5));
        p.setLocation("Lima");
        p.setNecessaryVolunteers(10);
        p.setEnrolledVolunteers(0);
        p.setStatus(VolunteerProgramStatus.ABIERTO); // ajusta según tu enum
        return p;
    }

    @Test
    @DisplayName("shouldFindVoluntarioWhenIdExists")
    void shouldFindVoluntarioWhenIdExists() {
        var v = voluntario("Ana", "Lopez", "ana@test.com");
        v = em.persistFlushFind(v);

        Optional<Volunteer> found = volunteerRepository.findById(v.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("ana@test.com");
    }

    @Test
    @DisplayName("shouldReturnEmptyWhenIdDoesNotExist")
    void shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<Volunteer> notFound = volunteerRepository.findById(123456L);
        assertThat(notFound).isEmpty();
    }

    @Test
    @DisplayName("shouldFindVoluntarioWhenEmailExists")
    void shouldFindVoluntarioWhenEmailExists() {
        var v = voluntario("Bruno", "Diaz", "bruno@test.com");
        em.persistAndFlush(v);

        Optional<Volunteer> found = volunteerRepository.findByEmail("bruno@test.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Bruno");
    }

    @Test
    @DisplayName("shouldReturnEmptyWhenEmailDoesNotExist")
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<Volunteer> notFound = volunteerRepository.findByEmail("noexiste@test.com");
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


        v.addInscription(p1);
        v.addInscription(p2);

        v = em.persistFlushFind(v);


        assertThat(v.getInscriptions()).hasSize(2);
        assertThat(v.getPrograms()).hasSize(2);
        assertThat(v.getPrograms())
                .extracting(VolunteerProgram::getName)
                .containsExactlyInAnyOrder("Rescate", "Adopciones");
    }

    @Test
    @DisplayName("shouldRemoveInscripcionWhenOrphanRemovalIsTrue")
    void shouldRemoveInscriptionWhenOrphanRemovalIsTrue() {
        var v = voluntario("Diego", "Suarez", "diego@test.com");
        var p = programa("Esterilizacion");
        p = em.persistFlushFind(p);

        v.addInscription(p);
        v = em.persistFlushFind(v);


        assertThat(v.getInscriptions()).hasSize(1);


        v.removeInscription(p);
        v = em.persistFlushFind(v);

        assertThat(v.getInscriptions()).isEmpty();
        assertThat(v.getPrograms()).isEmpty();

    }

}
