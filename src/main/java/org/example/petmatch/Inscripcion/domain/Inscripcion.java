package org.example.petmatch.Inscripcion.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.petmatch.Programa_voluntariado.Domain.ProgramaVoluntariado;
import org.example.petmatch.Voluntario.Domain.Voluntario;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Inscripcion {

    @EmbeddedId
    private VoluntarioProgramaId id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("voluntarioId")
    private Voluntario voluntario;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("programaId")
    private ProgramaVoluntariado programaVoluntariado;

    private ZonedDateTime fechaInscripcion = ZonedDateTime.now();

    public Inscripcion(Voluntario voluntario, ProgramaVoluntariado programaVoluntariado) {
        this.voluntario = voluntario;
        this.programaVoluntariado = programaVoluntariado;
        this.id = new VoluntarioProgramaId(voluntario.getId(), programaVoluntariado.getId());
    }
}
