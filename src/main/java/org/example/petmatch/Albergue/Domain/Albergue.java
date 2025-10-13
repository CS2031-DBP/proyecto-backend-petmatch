package org.example.petmatch.Albergue.Domain;

import org.example.petmatch.Post.Domain.Post;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaVoluntariado;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "albergues")
public class Albergue {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private Integer capacity;

    private Integer availableSpaces;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "albergue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "albergue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramaVoluntariado> programasVoluntariado;
}
