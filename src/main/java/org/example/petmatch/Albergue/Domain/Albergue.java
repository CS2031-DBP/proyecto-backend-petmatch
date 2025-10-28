package org.example.petmatch.Albergue.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.example.petmatch.Animales.Domain.Animal;
import org.example.petmatch.Post.Domain.Post;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaVoluntariado;
import org.example.petmatch.User.Domain.User;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "albergues")
@Builder
public class Albergue {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable  = false)
    private String password;

    private String description;

    private String address;

    private Long phone;

    private Integer capacity;

    private Integer availableSpaces;

    @Email
    @Column(nullable = false, unique = true)
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


    @Enumerated(EnumType.STRING)
    @Column(name = "rating")
    private Rating rating;
}
