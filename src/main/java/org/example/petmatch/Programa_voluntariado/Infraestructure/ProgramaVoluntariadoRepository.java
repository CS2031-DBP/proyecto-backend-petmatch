package org.example.petmatch.Programa_voluntariado.Infraestructure;

import org.example.petmatch.Programa_voluntariado.Domain.ProgramaVoluntariado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ProgramaVoluntariadoRepository extends JpaRepository<ProgramaVoluntariado, Long> {
    Optional<ProgramaVoluntariado> findById(Long id);
}
