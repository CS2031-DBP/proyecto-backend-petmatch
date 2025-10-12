package org.example.petmatch.Albergue.Infraestructure;

import org.example.petmatch.Albergue.Domain.Albergue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbergueRepository extends JpaRepository<Albergue, Long> {
    Optional<Albergue> findByName(String name);

    void deleteByName(String name);
}
