package org.example.petmatch.Inscription.infrastructure;

import org.example.petmatch.Inscription.domain.Inscription;
import org.example.petmatch.Inscription.domain.VolunteerProgramId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, VolunteerProgramId> {
    Optional<Inscription> findByVoluntarioIdAndProgramaVoluntariadoId(Long voluntarioId, Long programaId);
    boolean existsByVoluntarioIdAndProgramaVoluntariadoId(Long voluntarioId, Long programaId);
}
