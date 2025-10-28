package org.example.petmatch.Volunteer.Domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.petmatch.Inscription.domain.Inscription;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgramStatus;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.example.petmatch.User.Domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "voluntarios")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Volunteer extends User {
    @OneToMany(mappedBy = "voluntario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Inscription> inscripciones = new ArrayList<>();

    public List<VolunteerProgram> getProgramas(){
        List<VolunteerProgram> programas = new ArrayList<>();
        for(Inscription inscription : inscripciones){
            programas.add(inscription.getVolunteerProgram());
        }
        return programas;
    }

    public void addInscripcion(VolunteerProgram programa){
        Inscription inscription = new Inscription(this, programa);
        inscripciones.add(inscription);
        programa.getInscritos().add(inscription);
    }


    public void removeInscripcion(VolunteerProgram programa){
        for(Inscription inscription : inscripciones){
            if(inscription.getVolunteer().equals(this) && Objects.equals(inscription.getVolunteerProgram().getId(), programa.getId())){
                inscripciones.remove(inscription);
                programa.getInscritos().remove(inscription);
                inscription.setVolunteer(null);
                inscription.setVolunteerProgram(null);
                break;
            }
        }
        if (programa.getNumeroVoluntariosInscritos() > 0) {
            programa.setNumeroVoluntariosInscritos(programa.getNumeroVoluntariosInscritos() - 1);
        }
        if(programa.getStatus() == VolunteerProgramStatus.LLENO &&
                programa.getNumeroVoluntariosInscritos() < programa.getNumeroVoluntariosNecesarios()){
            programa.setStatus(VolunteerProgramStatus.ABIERTO);
        }
    }

}
