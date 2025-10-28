package org.example.petmatch.Comments.Controller;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Comments.DTO.CommentsResponseDTO;
import org.example.petmatch.Comments.Domain.CommentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    @GetMapping("/{user_name}")
    public ResponseEntity<List<CommentsResponseDTO>> getCommentsByUsername(@PathVariable("user_name") String username){
        List<CommentsResponseDTO> CommmentsByUser = commentsService.getAllComentsByUser(username);
        return ResponseEntity.ok(CommmentsByUser);
    }

    @GetMapping("/by_post/{post_name}")
    public ResponseEntity<List<CommentsResponseDTO>> getCommentsByPostTitle(@PathVariable("post_name") String post_title){
        List<CommentsResponseDTO> CommentsByPost = commentsService.getAllByPosttitle(post_title);
        return ResponseEntity.ok(CommentsByPost);
    }

}
