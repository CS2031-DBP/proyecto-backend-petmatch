package org.example.petmatch.Inscription.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.example.petmatch.Volunteer.Domain.Volunteer;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Inscription {

    @EmbeddedId
    private VolunteerProgramId id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("voluntarioId")
    private Volunteer volunteer;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("programaId")
    private VolunteerProgram volunteerProgram;

    private ZonedDateTime fechaInscripcion = ZonedDateTime.now();

    public Inscription(Volunteer volunteer, VolunteerProgram volunteerProgram) {
        this.volunteer = volunteer;
        this.volunteerProgram = volunteerProgram;
        this.id = new VolunteerProgramId(volunteer.getId(), volunteerProgram.getId());
    }
}
