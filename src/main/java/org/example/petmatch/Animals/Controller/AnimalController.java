package org.example.petmatch.Animals.Controller;

import org.example.petmatch.Exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.example.petmatch.Animals.DTO.AnimalPresentationDTO;
import org.example.petmatch.Animals.Domain.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/animales")
public class AnimalController {

    private final AnimalService animalService;

    @PatchMapping("/{animalName}/asignar/{shelterName}")
    public ResponseEntity<AnimalPresentationDTO> assignShelterToAnimal(
            @PathVariable String animalName,
            @PathVariable String shelterName) throws ValidationException {

        AnimalPresentationDTO updatedAnimal = animalService.assignShelterToAnimal(animalName, shelterName);
        return ResponseEntity.ok(updatedAnimal);
    }

    @GetMapping
    public ResponseEntity<List<AnimalPresentationDTO>> getAllAnimalsNoRegistered() {
        List<AnimalPresentationDTO> animals = animalService.getAllAnimalsNoRegistered();
        return ResponseEntity.ok(animals);
    }
}
