package org.example.petmatch.post;


import org.example.petmatch.Post.Domain.Post;
import org.example.petmatch.Post.Infraestructure.PostRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    PostRepository repository;

    private Shelter persistShelter(String name, String email) {
        Shelter shelter = Shelter.builder()
                .name(name)
                .email(email)
                .password("password123")
                .description("Refugio de prueba")
                .latitude(-12.0464)
                .longitude(-77.0428)
                .address("Av. Los Animales 123, Lima")
                .phone(999888777L)
                .capacity(40)
                .availableSpaces(20)
                .rating(Rating.ALTA)
                .build();
        return em.persistAndFlush(shelter);
    }

    private Post persistPost(String title, String necessities, Shelter shelter) {
        Post post = new Post();
        post.setTitle(title);
        post.setNecessities(necessities);
        post.setShelter(shelter);
        return em.persistAndFlush(post);
    }

    @Test
    public void ShouldFindByTitleWhenEntityIsPersisted() {

        Shelter shelter = persistShelter("Huellitas", "huellitas@refugio.com");
        Post saved = persistPost("Necesitamos mantas", "Mantas y croquetas", shelter);


        Optional<Post> found = repository.findByTitle("Necesitamos mantas");


        assertThat(found).isPresent();
        assertThat(found.get().getId()).isNotNull();
        assertThat(found.get().getTitle()).isEqualTo("Necesitamos mantas");
        assertThat(found.get().getNecessities()).isEqualTo("Mantas y croquetas");
        assertThat(found.get().getShelter()).isNotNull();
        assertThat(found.get().getShelter().getEmail()).isEqualTo("huellitas@refugio.com");
    }

    @Test
    public void ShouldReturnEmptyWhenTitleDoesNotExist() {

        Shelter shelter = persistShelter("Patitas", "patitas@refugio.com");
        persistPost("Medicinas para cachorros", "Antiparasitarios", shelter);


        Optional<Post> found = repository.findByTitle("Título inexistente");


        assertThat(found).isNotPresent();
    }

    @Test
    public void ShouldKeepShelterRelationWhenPostIsPersisted() {

        Shelter shelter = persistShelter("Rescate Lima", "rescate@refugio.com");
        Post saved = persistPost("Se requieren voluntarios", "Turnos de fin de semana", shelter);

        Post managed = em.find(Post.class, saved.getId());


        assertThat(managed).isNotNull();
        assertThat(managed.getShelter()).isNotNull();
        assertThat(managed.getShelter().getName()).isEqualTo("Rescate Lima");
    }

}
