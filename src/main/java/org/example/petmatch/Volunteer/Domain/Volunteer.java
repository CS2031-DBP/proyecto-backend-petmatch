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
@Table(name = "volunteers")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Volunteer extends User {
    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Inscription> inscriptions = new ArrayList<>();

    public List<VolunteerProgram> getPrograms(){
        List<VolunteerProgram> programas = new ArrayList<>();
        for(Inscription inscription : inscriptions){
            programas.add(inscription.getVolunteerProgram());
        }
        return programas;
    }


}
