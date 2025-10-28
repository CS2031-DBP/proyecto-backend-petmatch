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
@Table (name = "volunteerPrograms")
public class VolunteerProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private ZonedDateTime startDate;

    @Column(nullable = false)
    private ZonedDateTime finishDate;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer necessaryVolunteers;

    private Integer enrolledVolunteers = 0;

    @Enumerated(EnumType.STRING)
    private VolunteerProgramStatus status;


    @OneToMany(mappedBy = "volunteerProgram", orphanRemoval = true)
    private List<Inscription> enrolled = new ArrayList<>();

    public List<Volunteer> getVoluntarios(){
        List<Volunteer> volunteers = new ArrayList<>();
        for(Inscription inscription : enrolled){
            volunteers.add(inscription.getVolunteer());
        }
        return volunteers;
    }

    public boolean isLleno(){
        return status == VolunteerProgramStatus.LLENO;
    }


    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;
}
