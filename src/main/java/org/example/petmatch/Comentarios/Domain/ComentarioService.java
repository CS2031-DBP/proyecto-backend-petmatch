package org.example.petmatch.Comentarios.Domain;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Comentarios.DTO.ComentarioResponseDTO;
import org.example.petmatch.Comentarios.Infraestructure.ComentarioRepository;
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
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public List<ComentarioResponseDTO> getAllComentsByUser(String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User no encontrado"));

        List<Comentario> comentarios = user.getComentarios();

        return comentarios.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ComentarioResponseDTO> getAllByPosttitle(String postTitle) {
        Post post = postRepository.findByTitle(postTitle)
                .orElseThrow(() -> new NotFoundException("Post no encontrado"));

        List<Comentario> comentarios = post.getComentarios();

        return comentarios.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private ComentarioResponseDTO mapToResponseDTO(Comentario comentario) {
        ComentarioResponseDTO dto = new ComentarioResponseDTO();
        dto.setContent(comentario.getContent());
        dto.setPost_name(comentario.getPost().getTitle());
        dto.setUser_name(comentario.getUser().getName());
        return dto;
    }
}
