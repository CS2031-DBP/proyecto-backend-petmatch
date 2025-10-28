package org.example.petmatch.Post.Domain;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Shelter.Domain.Shelter;
import org.example.petmatch.Shelter.Infraestructure.ShelterRepository;
import org.example.petmatch.Exception.ValidationException;
import org.example.petmatch.Exception.NotFoundException;
import org.example.petmatch.Post.DTO.PostViewDTO;
import org.example.petmatch.Post.DTO.RequestPostDTO;
import org.example.petmatch.Post.Infraestructure.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {


    private final ShelterRepository shelterRepository;
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;

    public List<PostViewDTO> getALLPosts(){

        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> modelMapper.map(post, PostViewDTO.class))
                .toList();
    }
    @Transactional
    public Post NewPost(String albergue_name, RequestPostDTO postDTO) throws ValidationException {
        if (postRepository.findByTitle(postDTO.getTitle()).isPresent()) {
            throw new ValidationException("Existe ya un post con este nombre");
        }
        Shelter shelter = shelterRepository.findByName(albergue_name).orElseThrow(()-> new NotFoundException("Albergue no encontrado"));

        Post post = modelMapper.map(postDTO, Post.class);
        post.setShelter(shelter);
        shelter.getPosts().add(post);
        postRepository.save(post);
        return post;
    }


    public void deletePost(String post_title) throws NotFoundException {
        Post post = postRepository.findByTitle(post_title).orElseThrow(()-> new NotFoundException("Post no encontrado"));
        postRepository.delete(post);
    }


    public PostViewDTO getPostByName(String post_title) throws NotFoundException{
        Post post = postRepository.findByTitle(post_title).orElseThrow(()-> new NotFoundException("Post no encontrado"));
        return modelMapper.map(post, PostViewDTO.class);
    }

    public List<PostViewDTO> getPostsByAlbergue(String albergueName) {
        Shelter shelter_requerido = shelterRepository.findByName(albergueName).orElseThrow(()-> new NotFoundException("Albergue no encontrado"));
        return shelter_requerido.getPosts().stream()
                .map(post -> modelMapper.map(post, PostViewDTO.class))
                .toList();

    }
}