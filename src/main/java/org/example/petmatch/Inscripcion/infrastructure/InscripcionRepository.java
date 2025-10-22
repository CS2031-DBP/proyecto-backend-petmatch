package org.example.petmatch.Inscripcion.infrastructure;

import org.example.petmatch.Inscripcion.domain.Inscripcion;
import org.example.petmatch.Inscripcion.domain.VoluntarioProgramaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, VoluntarioProgramaId> {
    Optional<Inscripcion> findByVoluntarioIdAndProgramaVoluntariadoId(Long voluntarioId, Long programaId);
    boolean existsByVoluntarioIdAndProgramaVoluntariadoId(Long voluntarioId, Long programaId);
}
