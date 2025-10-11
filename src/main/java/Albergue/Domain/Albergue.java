package Albergue.Domain;

import Animales.Domain.Animal;
import Post.Domain.Post;
import Programa_voluntariado.Domain.ProgramaVoluntariado;
import User.Domain.User;
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

    private String password;

    private String description;

    private String address;

    private Integer phone;

    private Integer capacity;

    private Integer availableSpaces;

    private String email;

    @OneToMany(mappedBy = "albergue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "albergue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramaVoluntariado> programasVoluntariado;

    @OneToMany(mappedBy = "albergue")
    private List<Animal> animales;
    // Metodo de reasignacion

    @ManyToMany
    @JoinTable(name = "Albergue_seguidores", joinColumns = @JoinColumn(name = "albergue_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> seguidores;


    @Enumerated
    @Column(name = "rating")
    private Rating rating;
}
