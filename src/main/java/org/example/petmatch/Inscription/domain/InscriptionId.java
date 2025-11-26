package org.example.petmatch.Inscription.domain;

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
public class InscriptionId implements Serializable {

    @Column
    private Long volunteerId;

    @Column
    private Long programId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InscriptionId that = (InscriptionId) o;
        return Objects.equals(volunteerId, that.volunteerId) && Objects.equals(programId, that.programId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(volunteerId, programId);
    }

}
