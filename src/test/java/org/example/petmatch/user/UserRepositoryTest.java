package org.example.petmatch.user;


import org.example.petmatch.User.Domain.Role;
import org.example.petmatch.User.Domain.User;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private UserRepository repository;

    private User persistUser(String name, String lastname, String email) {
        User user = User.builder()
                .name(name)
                .lastname(lastname)
                .email(email)
                .password("password123")
                .role(Role.USER)
                .build();

        return em.persistAndFlush(user);
    }

    @Test
    public void ShouldFindByEmailWhenEntityIsPersisted() {
        User saved = persistUser("Valentín", "Tuesta", "valentin@petmatch.com");

        Optional<User> found = repository.findByEmail("valentin@petmatch.com");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isNotNull();
        assertThat(found.get().getEmail()).isEqualTo(saved.getEmail());
        assertThat(found.get().getName()).isEqualTo("Valentín");
        assertThat(found.get().getRole()).isEqualTo(Role.USER);
        assertThat(found.get().getPassword()).isEqualTo("password123");
    }

    @Test
    public void ShouldFindByNameWhenEntityIsPersisted() {

        persistUser("Ana", "García", "ana@petmatch.com");


        Optional<User> found = repository.findByName("Ana");


        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ana");
        assertThat(found.get().getEmail()).isEqualTo("ana@petmatch.com");
        assertThat(found.get().getRole()).isEqualTo(Role.USER);
    }

    @Test
    public void ShouldExistByEmailWhenEntityIsPersisted() {

        persistUser("Juan", "Pérez", "juan@petmatch.com");


        boolean exists = repository.existsByEmail("juan@petmatch.com");


        assertThat(exists).isTrue();
    }

    @Test
    public void ShouldReturnFalseWhenEmailDoesNotExist() {

        persistUser("Luis", "Lopez", "luis@petmatch.com");


        boolean exists = repository.existsByEmail("no-existe@petmatch.com");


        assertThat(exists).isFalse();
    }

    @Test
    public void ShouldReturnEmptyWhenNameDoesNotExist() {

        persistUser("Carla", "Mendoza", "carla@petmatch.com");


        Optional<User> found = repository.findByName("NombreInexistente");


        assertThat(found).isNotPresent();
    }

    @Test
    public void ShouldReturnEmptyWhenEmailDoesNotExist() {

        persistUser("Mario", "Rossi", "mario@petmatch.com");


        Optional<User> found = repository.findByEmail("nada@petmatch.com");


        assertThat(found).isNotPresent();
    }
}
