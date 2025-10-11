package VoluntarioPrograma.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoluntarioProgramaId implements Serializable {

    @Column
    private Long voluntarioId;
    @Column
    private Long programaId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VoluntarioProgramaId that = (VoluntarioProgramaId) o;
        return Objects.equals(voluntarioId, that.voluntarioId) && Objects.equals(programaId, that.programaId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(voluntarioId, programaId);
    }



}
