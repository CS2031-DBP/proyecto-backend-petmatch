package org.example.petmatch.inscription;

import org.example.petmatch.Inscription.domain.Inscription;
import org.example.petmatch.Inscription.domain.InscriptionId;
import org.example.petmatch.Inscription.infrastructure.InscriptionRepository;
import org.example.petmatch.Shelter.Domain.Rating;
import org.example.petmatch.Shelter.Domain.Shelter;
import org.example.petmatch.User.Domain.Role;
import org.example.petmatch.Volunteer.Domain.Volunteer;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class InscriptionRepositoryTest {
    @Autowired
    TestEntityManager em;

    @Autowired
    InscriptionRepository repository;

    private Volunteer persistVolunteer(String name, String email) {

        Volunteer v = new Volunteer();

        v.setName(name);
        v.setLastname("Tester");
        v.setEmail(email);
        v.setPassword("password123");
        v.setRole(Role.USER);


        return em.persistAndFlush(v);
    }

    private VolunteerProgram persistVolunteerProgram(String nombre) {

        Shelter s = Shelter.builder()
                .name("Refugio " + nombre)
                .email("refugio_" + nombre.replaceAll("\\s+","").toLowerCase() + "@petmatch.com")
                .password("password123")
                .description("Refugio de prueba")
                .latitude(-12.0464)
                .longitude(-77.0428)
                .address("Av. Los Animales 123")
                .phone(999111222L)
                .capacity(30)
                .availableSpaces(10)
                .rating(Rating.ALTA)
                .build();
        s = em.persistAndFlush(s);

        VolunteerProgram p = new VolunteerProgram();

        p.setName(nombre);
        p.setLocation("Lima");
        p.setDescription("Descripción " + nombre);
        p.setStartDate(ZonedDateTime.now().plusDays(1));
        p.setFinishDate(ZonedDateTime.now().plusDays(30));
        p.setNecessaryVolunteers(10);    // si tu campo se llama distinto, ajusta
        p.setEnrolledVolunteers(0);      // idem
        p.setStatus(VolunteerProgramStatus.ABIERTO);
        p.setShelter(s);                 // si tu entidad lo requiere

        return em.persistAndFlush(p);
    }

    private Inscription persistInscription(Volunteer volunteer, VolunteerProgram program) {
        Inscription ins = new Inscription(volunteer, program);
        ins.setFechaInscripcion(ZonedDateTime.now());
        return em.persistAndFlush(ins);
    }

    @Test
    public void ShouldFindByVolunteerIdAndVolunteerProgramIdWhenEntityIsPersisted() {

        Volunteer volunteer = persistVolunteer("Valentín", "valentin@petmatch.com");
        VolunteerProgram program = persistVolunteerProgram("Programa A");
        Inscription insc = persistInscription(volunteer, program);


        Optional<Inscription> found = repository.findByVolunteerIdAndVolunteerProgramId(
                volunteer.getId(), program.getId()
        );


        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(new InscriptionId(volunteer.getId(), program.getId()));
        assertThat(found.get().getVolunteer().getId()).isEqualTo(volunteer.getId());
        assertThat(found.get().getVolunteerProgram().getId()).isEqualTo(program.getId());
    }

    @Test
    public void ShouldExistByVolunteerIdAndVolunteerProgramIdWhenEntityIsPersisted() {

        Volunteer volunteer = persistVolunteer("Ana", "ana@petmatch.com");
        VolunteerProgram program = persistVolunteerProgram("Programa B");
        persistInscription(volunteer, program);


        boolean exists = repository.existsByVolunteerIdAndVolunteerProgramId(
                volunteer.getId(), program.getId()
        );


        assertThat(exists).isTrue();
    }

    @Test
    public void ShouldReturnFalseWhenPairDoesNotExist() {

        Volunteer volunteer = persistVolunteer("Juan", "juan@petmatch.com");
        VolunteerProgram program = persistVolunteerProgram("Programa C");



        boolean exists = repository.existsByVolunteerIdAndVolunteerProgramId(
                volunteer.getId(), program.getId()
        );


        assertThat(exists).isFalse();
    }

    @Test
    public void ShouldReturnEmptyWhenPairDoesNotExist() {

        Volunteer volunteer = persistVolunteer("Carla", "carla@petmatch.com");
        VolunteerProgram program = persistVolunteerProgram("Programa D");



        Optional<Inscription> found = repository.findByVolunteerIdAndVolunteerProgramId(
                volunteer.getId(), program.getId()
        );


        assertThat(found).isNotPresent();
    }
}
