package org.example.petmatch.Animals.Infraestructure;

import org.example.petmatch.Animals.Domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

}
