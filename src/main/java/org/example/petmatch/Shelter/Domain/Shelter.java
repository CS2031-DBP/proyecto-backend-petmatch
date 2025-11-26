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
@Table(name = "shelters")
@Builder
public class Shelter {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable  = false)
    private String password;

    private String description;

    private Double latitude;

    private Double longitude;

    private String address;


    private Long phone;

    private Integer capacity;

    private Integer availableSpaces;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VolunteerProgram> volunteerPrograms;

    @OneToMany(mappedBy = "shelter")
    private List<Animal> animals;

    @ManyToMany
    @JoinTable(name = "shelter_followers", joinColumns = @JoinColumn(name = "shelter_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> followers;


    @Enumerated(EnumType.STRING)
    @Column(name = "rating")
    private Rating rating;
}
