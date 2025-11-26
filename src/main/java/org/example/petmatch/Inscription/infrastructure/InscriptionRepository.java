package org.example.petmatch.Inscription.infrastructure;

import org.example.petmatch.Inscription.domain.Inscription;
import org.example.petmatch.Inscription.domain.InscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, InscriptionId> {
    Optional<Inscription> findByVolunteerIdAndVolunteerProgramId(Long volunteerId, Long volunteerProgramId);
    boolean existsByVolunteerIdAndVolunteerProgramId(Long volunteerId, Long volunteerProgramId);
}
