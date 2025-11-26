package org.example.petmatch.Comments.Domain;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Comments.DTO.CommentsResponseDTO;
import org.example.petmatch.Comments.Infraestructure.CommentsRepository;
import org.example.petmatch.Exception.NotFoundException;
import org.example.petmatch.Post.Domain.Post;
import org.example.petmatch.Post.Infraestructure.PostRepository;
import org.example.petmatch.User.Domain.User;
import org.example.petmatch.User.Exceptions.UserNotFoundException;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public List<CommentsResponseDTO> getAllComentsByUser(String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User no encontrado"));

        List<Comments> comments = user.getComments();

        return comments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CommentsResponseDTO> getAllByPosttitle(String postTitle) {
        Post post = postRepository.findByTitle(postTitle)
                .orElseThrow(() -> new NotFoundException("Post no encontrado"));

        List<Comments> comments = post.getComments();

        return comments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private CommentsResponseDTO mapToResponseDTO(Comments comments) {
        CommentsResponseDTO dto = new CommentsResponseDTO();
        dto.setContent(comments.getContent());
        dto.setPost_name(comments.getPost().getTitle());
        dto.setUser_name(comments.getUser().getName());
        return dto;
    }
}
