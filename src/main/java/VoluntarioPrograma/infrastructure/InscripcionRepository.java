package VoluntarioPrograma.infrastructure;

import VoluntarioPrograma.domain.Inscripcion;
import VoluntarioPrograma.domain.VoluntarioProgramaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, VoluntarioProgramaId> {

    boolean existsById(VoluntarioProgramaId id);

    boolean existsByVoluntarioIdAndProgramaVoluntariadoId(Long voluntarioId, Long programaId);
}
