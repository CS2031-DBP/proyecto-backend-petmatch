package org.example.petmatch.Albergue.Infraestructure;

import org.example.petmatch.Albergue.Domain.Albergue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbergueRepository extends JpaRepository<Albergue, Long> {
    Optional<Albergue> findByName(String name);
    Optional<Albergue> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByName(String name);
}
