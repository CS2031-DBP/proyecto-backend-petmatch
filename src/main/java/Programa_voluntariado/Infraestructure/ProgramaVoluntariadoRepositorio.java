package Programa_voluntariado.Infraestructure;

import Programa_voluntariado.Domain.ProgramaVoluntariado;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ProgramaVoluntariadoRepositorio extends JpaRepository<ProgramaVoluntariado, Long> {
    public Optional<ProgramaVoluntariado> findById(Long id);
}
