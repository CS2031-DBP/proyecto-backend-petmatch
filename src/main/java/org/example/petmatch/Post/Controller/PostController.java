package org.example.petmatch.Post.Controller;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Common.NewIdDTO;
import org.example.petmatch.Exception.ValidationException;
import org.example.petmatch.Post.DTO.PostViewDTO;
import org.example.petmatch.Post.DTO.RequestPostDTO;
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
    @GetMapping("/{post_title}")
    public ResponseEntity<PostViewDTO> getPost(@PathVariable String post_title){
        PostViewDTO post = postService.getPostByName(post_title);
        return ResponseEntity.ok(post);

    }

    @GetMapping("/albergue/{albergue_name}")
    public ResponseEntity<List<PostViewDTO>> getPostsByAlbergue(@PathVariable String albergue_name) throws ValidationException {
        List<PostViewDTO> posts = postService.getPostsByAlbergue(albergue_name);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{albergue_name}")
    public ResponseEntity<NewIdDTO> createPost(@PathVariable String albergue_name, @RequestBody RequestPostDTO postDTO) throws Exception {
        Post post_created = postService.NewPost(albergue_name, postDTO);
        return ResponseEntity.created(null).body(new NewIdDTO(post_created.getId()));

    }

    @DeleteMapping("/delete/{post_name}")
    public ResponseEntity<Void> deletePost(@PathVariable String post_name) throws Exception {
        postService.deletePost(post_name);
        return ResponseEntity.noContent().build();
    }
}
