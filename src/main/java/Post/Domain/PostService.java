package Post.Domain;

import Post.DTO.PostViewDTO;
import Post.Infraestructure.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {


    private ModelMapper modelMapper;
    private PostRepository postRepository;

    public PostService(ModelMapper modelMapper, PostRepository postRepository) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
    }

    public List<PostViewDTO> getALLPosts(){

        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> modelMapper.map(post, PostViewDTO.class))
                .toList();
    }

    public Post NewPost(String albergue_name, Post post){

    }


}
