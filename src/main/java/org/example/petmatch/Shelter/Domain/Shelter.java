package org.example.petmatch.Shelter.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.example.petmatch.Animals.Domain.Animal;
import org.example.petmatch.Post.Domain.Post;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.example.petmatch.User.Domain.User;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "albergues")
@Builder
public class Shelter {

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
    private List<VolunteerProgram> programasVoluntariado;

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
