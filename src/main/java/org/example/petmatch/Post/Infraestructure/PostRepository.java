package org.example.petmatch.Post.Infraestructure;

import org.example.petmatch.Post.Domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByTitle(String title);
}
