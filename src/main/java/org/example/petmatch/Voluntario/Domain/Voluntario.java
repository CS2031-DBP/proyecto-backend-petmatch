package org.example.petmatch.Voluntario.Domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.petmatch.Inscripcion.domain.Inscripcion;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaVoluntariado;
import org.example.petmatch.User.Domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "voluntarios")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Voluntario extends User {
    @OneToMany(mappedBy = "voluntario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public List<ProgramaVoluntariado> getProgramas(){
        List<ProgramaVoluntariado> programas = new ArrayList<>();
        for(Inscripcion inscripcion : inscripciones){
            programas.add(inscripcion.getProgramaVoluntariado());
        }
        return programas;
    }

    public void addInscripcion(ProgramaVoluntariado programa){
        Inscripcion inscripcion = new Inscripcion(this, programa);
        inscripciones.add(inscripcion);
        programa.getInscritos().add(inscripcion);
    }

    public void removeInscripcion(ProgramaVoluntariado programa){
        for(Inscripcion inscripcion : inscripciones){
            if(inscripcion.getVoluntario().equals(this) && Objects.equals(inscripcion.getProgramaVoluntariado().getId(), programa.getId())){
                inscripciones.remove(inscripcion);
                programa.getInscritos().remove(inscripcion);
                inscripcion.setVoluntario(null);
                inscripcion.setProgramaVoluntariado(null);
                break;
            }
        }
    }

}
