package org.example.petmatch.shelter;

import org.example.petmatch.Shelter.Domain.Rating;
import org.example.petmatch.Shelter.Domain.Shelter;
import org.example.petmatch.Shelter.Infraestructure.ShelterRepository;
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
public class ShelterRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ShelterRepository repository;

    private Shelter persistShelter(String name, String email) {
        Shelter shelter = Shelter.builder()
                .name(name)
                .email(email)
                .password("password123") // obligatorio
                .description("Refugio de prueba")
                .latitude(-12.0464)
                .longitude(-77.0428)
                .address("Av. Los Animales 123, Lima")
                .phone(987654321L)
                .capacity(50)
                .availableSpaces(25)
                .rating(Rating.ALTA)
                .build();

        return em.persistAndFlush(shelter);
    }

    @Test
    public void ShouldFindByNameWhenEntityIsPersisted() {

        Shelter saved = persistShelter("Huellitas", "huellitas@refugio.com");


        Optional<Shelter> found = repository.findByName("Huellitas");


        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Huellitas");
        assertThat(found.get().getEmail()).isEqualTo("huellitas@refugio.com");
    }

    @Test
    public void ShouldFindByEmailWhenEntityIsPersisted() {

        Shelter saved = persistShelter("Patitas Felices", "patitas@refugio.com");


        Optional<Shelter> found = repository.findByEmail("patitas@refugio.com");


        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("patitas@refugio.com");
        assertThat(found.get().getPassword()).isEqualTo("password123");
    }

    @Test
    public void ShouldExistByEmailWhenEntityIsPersisted() {

        persistShelter("Mi Refugio", "contacto@refugio.com");


        boolean exists = repository.existsByEmail("contacto@refugio.com");


        assertThat(exists).isTrue();
    }

    @Test
    public void ShouldReturnFalseWhenEmailDoesNotExist() {

        persistShelter("Esperanza Animal", "esperanza@refugio.com");


        boolean exists = repository.existsByEmail("inexistente@refugio.com");


        assertThat(exists).isFalse();
    }

    @Test
    public void ShouldReturnEmptyWhenNameDoesNotExist() {

        persistShelter("Rescate Lima", "rescate@refugio.com");


        Optional<Shelter> found = repository.findByName("NoExiste");


        assertThat(found).isNotPresent();
    }

    @Test
    public void ShouldReturnEmptyWhenEmailDoesNotExist() {

        persistShelter("Protectora Callao", "callao@refugio.com");


        Optional<Shelter> found = repository.findByEmail("otro@refugio.com");


        assertThat(found).isNotPresent();
    }

    @Test
    public void ShouldDeleteByNameWhenEntityExists() {

        Shelter saved = persistShelter("Animales Unidos", "unidos@refugio.com");


        repository.deleteByName("Animales Unidos");
        Optional<Shelter> found = repository.findByName("Animales Unidos");


        assertThat(found).isNotPresent();
    }
}
