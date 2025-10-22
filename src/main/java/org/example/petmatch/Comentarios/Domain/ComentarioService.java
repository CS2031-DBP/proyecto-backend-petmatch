package org.example.petmatch.Comentarios.Domain;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Comentarios.DTO.ComentarioResponseDTO;
import org.example.petmatch.Comentarios.Infraestructure.ComentarioRepository;
import org.example.petmatch.Exception.NotFoundException;
import org.example.petmatch.Post.Domain.Post;
import org.example.petmatch.Post.Infraestructure.PostRepository;
import org.example.petmatch.User.Domain.User;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public List<ComentarioResponseDTO> getAllComentsByUser(String username) throws NotFoundException {
        User user = userRepository.findByName(username).orElseThrow(()-> new NotFoundException("User no encontrado"));
        List<Comentario> coments = user.getComentarios();
        return coments.stream()
                .map(comentario -> modelMapper.map(comentario, ComentarioResponseDTO.class))
                .toList();
    }

    public List<ComentarioResponseDTO> getAllByPosttitle(String postTitle) throws NotFoundException {
        Post post = postRepository.findByTitle(postTitle).orElseThrow(()-> new NotFoundException("Post no encontrado"));
        List<Comentario> coments = post.getComentarios();
        return coments.stream()
                .map(comentario -> modelMapper.map(comentario, ComentarioResponseDTO.class))
                .toList();
    }


}
