package org.example.petmatch.Shelter.Infraestructure;

import org.example.petmatch.Shelter.Domain.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    Optional<Shelter> findByName(String name);
    Optional<Shelter> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByName(String name);
}
