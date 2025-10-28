package org.example.petmatch.Volunteer_Program.Domain;


import org.example.petmatch.Shelter.Domain.Shelter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.petmatch.Inscription.domain.Inscription;
import org.example.petmatch.Volunteer.Domain.Volunteer;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "programas_de_voluntariado")
public class VolunteerProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private ZonedDateTime fechaInicio;

    @Column(nullable = false)
    private ZonedDateTime fechaFin;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private Integer numeroVoluntariosNecesarios;

    private Integer numeroVoluntariosInscritos = 0;

    @Enumerated(EnumType.STRING)
    private VolunteerProgramStatus status;


    @OneToMany(mappedBy = "programaVoluntariado", orphanRemoval = true)
    private List<Inscription> inscritos = new ArrayList<>();

    public List<Volunteer> getVoluntarios(){
        List<Volunteer> volunteers = new ArrayList<>();
        for(Inscription inscription : inscritos){
            volunteers.add(inscription.getVolunteer());
        }
        return volunteers;
    }

    public boolean isLleno(){
        return status == VolunteerProgramStatus.LLENO;
    }


    @ManyToOne
    @JoinColumn(name = "albergue_id")
    private Shelter shelter;
}
