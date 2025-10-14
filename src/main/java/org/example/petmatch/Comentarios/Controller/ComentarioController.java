package org.example.petmatch.Comentarios.Controller;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Comentarios.Domain.Comentario;
import org.example.petmatch.Comentarios.Infraestructure.ComentarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioRepository comentarioRepository;

    @GetMapping("/{user_name}")
    public ResponseEntity<List<Comentario>> getComentariosByUsername(@PathVariable("user_name") String username){
        List<Comentario> CommmentsByUser = comentarioRepository.getAllComentsByUser(username);
        return ResponseEntity.ok(CommmentsByUser);
    }

    @GetMapping("/by_post/{post_name}")
    public ResponseEntity<List<Comentario>>


}
