package org.example.petmatch.Comments.Infraestructure;

import org.example.petmatch.Comments.Domain.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

}
