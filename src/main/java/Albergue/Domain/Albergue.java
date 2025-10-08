package Albergue.Domain;

import Post.Domain.Post;
import Programa_voluntariado.Domain.ProgramaVoluntariado;
import jakarta.persistence.*;
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

    private Integer phone;

    private Integer capacity;

    private Integer availableSpaces;

    @OneToMany(mappedBy = "albergue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "albergue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramaVoluntariado> programasVoluntariado;
}
