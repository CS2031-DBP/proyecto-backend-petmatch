package Voluntario.Infraestructure;

import Voluntario.Domain.Voluntario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
}
