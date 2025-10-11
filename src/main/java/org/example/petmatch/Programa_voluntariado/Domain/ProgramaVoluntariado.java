package org.example.petmatch.Programa_voluntariado.Domain;


import org.example.petmatch.Albergue.Domain.Albergue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "programas_de_voluntariado")
public class ProgramaVoluntariado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "albergue_id")
    private Albergue albergue;
}
