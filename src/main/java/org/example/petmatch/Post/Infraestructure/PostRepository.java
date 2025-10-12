package org.example.petmatch.Post.Infraestructure;

import org.example.petmatch.Post.Domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
