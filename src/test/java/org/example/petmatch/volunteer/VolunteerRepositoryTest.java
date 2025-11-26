package org.example.petmatch.volunteer;


import org.example.petmatch.User.Domain.Role;
import org.example.petmatch.Volunteer.Domain.Volunteer;
import org.example.petmatch.Volunteer.Infraestructure.VolunteerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class VolunteerRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private VolunteerRepository repository;

    @Test
    public void ShouldFindByIdWhenEntityIsPersisted() {
        var v = new Volunteer();
        v.setName("juan");
        v.setLastname("perez");
        v.setEmail("jp@test.com");
        v.setPassword("password123");
        v.setRole(Role.USER);
        em.persistAndFlush(v);
        assertTrue(repository.findById(v.getId()).isPresent());
    }

    @Test
    public void ShouldFindByEmailWhenEntityIsPersisted() {
        var v = new Volunteer();
        v.setName("juan");
        v.setLastname("perez");
        v.setEmail("jp@test.com");
        v.setPassword("password123");
        v.setRole(Role.USER);
        em.persistAndFlush(v);
        assertTrue(repository.findByEmail("jp@test.com").isPresent());
    }

}
