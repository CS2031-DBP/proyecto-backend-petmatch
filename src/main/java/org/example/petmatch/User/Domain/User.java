package org.example.petmatch.User.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.example.petmatch.Comments.Domain.Comments;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column( nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments;

}
