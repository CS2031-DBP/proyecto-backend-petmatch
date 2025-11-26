package org.example.petmatch.animal;

import org.example.petmatch.Animals.Domain.Animal;
import org.example.petmatch.Animals.Infraestructure.AnimalRepository;
import org.example.petmatch.Shelter.Domain.Rating;
import org.example.petmatch.Shelter.Domain.Shelter;
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
public class AnimalRepositoryTest {
    @Autowired
    TestEntityManager em;

    @Autowired
    AnimalRepository repository;

    private Shelter persistShelter(String name, String email) {
        Shelter shelter = Shelter.builder()
                .name(name)
                .email(email)
                .password("password123")
                .description("Refugio de prueba")
                .latitude(-12.0464)
                .longitude(-77.0428)
                .address("Av. Los Animales 123")
                .phone(999888777L)
                .capacity(40)
                .availableSpaces(20)
                .rating(Rating.ALTA)
                .build();
        return em.persistAndFlush(shelter);
    }

    private Animal persistAnimal(String name, String breed, Shelter shelter) {
        Animal a = new Animal();
        a.setName(name);
        a.setBreed(breed);
        a.setRegistered(false);
        a.setShelter(shelter);
        return em.persistAndFlush(a);
    }

    @Test
    public void ShouldFindByNameWhenEntityIsPersisted() {

        Shelter shelter = persistShelter("Huellitas", "huellitas@refugio.com");
        Animal saved = persistAnimal("Bruno", "Labrador", shelter);


        Optional<Animal> found = repository.findByName("Bruno");


        assertThat(found).isPresent();
        assertThat(found.get().getId()).isNotNull();
        assertThat(found.get().getName()).isEqualTo("Bruno");
        assertThat(found.get().getBreed()).isEqualTo("Labrador");
        assertThat(found.get().getRegistered()).isFalse(); // valor por defecto de tu entidad
        assertThat(found.get().getShelter()).isNotNull();
        assertThat(found.get().getShelter().getEmail()).isEqualTo("huellitas@refugio.com");
    }

    @Test
    public void ShouldReturnEmptyWhenNameDoesNotExist() {

        Shelter shelter = persistShelter("Patitas", "patitas@refugio.com");
        persistAnimal("Luna", "Mestizo", shelter);


        Optional<Animal> found = repository.findByName("NombreInexistente");


        assertThat(found).isNotPresent();
    }

    @Test
    public void ShouldKeepShelterRelationWhenAnimalIsPersisted() {

        Shelter shelter = persistShelter("Rescate Lima", "rescate@refugio.com");
        Animal saved = persistAnimal("Max", "Husky", shelter);

        Animal managed = em.find(Animal.class, saved.getId());


        assertThat(managed).isNotNull();
        assertThat(managed.getShelter()).isNotNull();
        assertThat(managed.getShelter().getName()).isEqualTo("Rescate Lima");
    }
}
