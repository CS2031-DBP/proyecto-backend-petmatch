package org.example.petmatch.Voluntario.Infraestructure;

import org.example.petmatch.Voluntario.Domain.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    Optional<Voluntario> findById(Long id);
    Optional<Voluntario> findByEmail(String email);
}
