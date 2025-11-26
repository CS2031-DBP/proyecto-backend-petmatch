package org.example.petmatch.Animals.Infraestructure;

import org.example.petmatch.Animals.Domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByName(String name);
}
