package org.example.petmatch.User.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.petmatch.Albergue.Domain.Albergue;
import org.example.petmatch.Comentarios.Domain.Comentario;
import org.example.petmatch.Post.Domain.Post;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String Lastname;

    private String email;

    private String password;

    @ManyToMany(mappedBy = "albergues_seguidos")
    private List<Albergue> albergues_seguidos;

    @OneToMany(mappedBy = "User")
    private List<Comentario> comentarios ;

}
