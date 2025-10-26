package org.example.petmatch.Comentarios.Infraestructure;

import org.example.petmatch.Comentarios.Domain.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

}
