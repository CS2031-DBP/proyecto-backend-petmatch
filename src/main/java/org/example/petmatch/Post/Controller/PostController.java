package org.example.petmatch.Post.Controller;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Common.NewIdDTO;
import org.example.petmatch.Post.DTO.PostViewDTO;
import org.example.petmatch.Post.Domain.Post;
import org.example.petmatch.Post.Domain.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostViewDTO>> getAllThePosts(){
        List<PostViewDTO> posts = postService.getALLPosts();
        return ResponseEntity.ok(posts);

    }

    @PostMapping("/{albergue_name}")
    public ResponseEntity<NewIdDTO> createPost(@PathVariable String albergue_name, @RequestBody Post post){
        Post post_created = postService.NewPost(albergue_name, post);
        return ResponseEntity.created(null).body(new NewIdDTO(post_created.getId()));

    }
}
