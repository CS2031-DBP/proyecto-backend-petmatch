package org.example.petmatch.Animales.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.petmatch.Albergue.Domain.Albergue;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean registered = false;

    private String name;

    private String breed;

    @ManyToOne
    @JoinColumn(name = "albergue_id")
    private Albergue albergue;

}
