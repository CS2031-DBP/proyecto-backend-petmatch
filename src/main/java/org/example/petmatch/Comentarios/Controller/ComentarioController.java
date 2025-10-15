package org.example.petmatch.Comentarios.Controller;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Comentarios.DTO.ComentarioResponseDTO;
import org.example.petmatch.Comentarios.Domain.Comentario;
import org.example.petmatch.Comentarios.Domain.ComentarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @GetMapping("/{user_name}")
    public ResponseEntity<List<ComentarioResponseDTO>> getComentariosByUsername(@PathVariable("user_name") String username){
        List<ComentarioResponseDTO> CommmentsByUser = comentarioService.getAllComentsByUser(username);
        return ResponseEntity.ok(CommmentsByUser);
    }

    @GetMapping("/by_post/{post_name}")
    public ResponseEntity<List<ComentarioResponseDTO>> getComentariosByPostTitle(@PathVariable("post_name") String post_title){
        List<ComentarioResponseDTO> CommentsByPost = comentarioService.getAllByPosttitle(post_title);
        return ResponseEntity.ok(CommentsByPost);
    }

}
