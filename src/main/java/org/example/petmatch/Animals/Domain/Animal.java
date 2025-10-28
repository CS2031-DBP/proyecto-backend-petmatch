package org.example.petmatch.Animals.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.petmatch.Shelter.Domain.Shelter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean registered = false;

    private String name;

    private String breed;

    private String image;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

}
