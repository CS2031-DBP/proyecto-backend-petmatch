package org.example.petmatch.Post.Domain;

import org.example.petmatch.Albergue.Domain.Albergue;
import org.example.petmatch.Albergue.Domain.AlbergueService;
import org.example.petmatch.Albergue.Infraestructure.AlbergueRepository;
import org.example.petmatch.Common.ValidationException;
import org.example.petmatch.Post.DTO.PostViewDTO;
import org.example.petmatch.Post.Infraestructure.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {


    private final AlbergueRepository albergueRepository;
    private ModelMapper modelMapper;
    private PostRepository postRepository;


    public PostService(ModelMapper modelMapper, PostRepository postRepository, AlbergueRepository albergueRepository) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.albergueRepository = albergueRepository;
    }

    public List<PostViewDTO> getALLPosts(){

        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> modelMapper.map(post, PostViewDTO.class))
                .toList();
    }

    public Post NewPost(String albergue_name, Post post) throws ValidationException {

        Albergue albergue = albergueRepository.findByName(albergue_name).orElseThrow(()-> new ValidationException("Albergue not found"));
    }


}