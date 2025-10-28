package org.example.petmatch.volunteerProgram;

import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramStatus;
import org.example.petmatch.Volunteer_Program.Infraestructure.VolunteerProgramRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class VolunteerProgramRepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private VolunteerProgramRepository repository;

    @Test
    public void ShouldFindByIdWhenEntityIsPersisted() {
        var p = new VolunteerProgram();
        p.setName("Demo");
        p.setDescription("Desc");
        p.setStartDate(ZonedDateTime.now());
        p.setFinishDate(ZonedDateTime.now().plusDays(1));
        p.setLocation("Lima");
        p.setNecessaryVolunteers(5);
        p.setEnrolledVolunteers(0);
        p.setStatus(VolunteerProgramStatus.ABIERTO);

        em.persistAndFlush(p);

        assertTrue(repository.findById(p.getId()).isPresent());
    }

    @Test
    public void ShouldReturnEmptyWhenIdDoesNotExist() {
        assertTrue(repository.findById(123L).isEmpty());
    }
}
