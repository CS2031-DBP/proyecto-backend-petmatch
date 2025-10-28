package org.example.petmatch.Animals.Controller;

import org.example.petmatch.Animals.DTO.AnimalPresentationDTO;
import org.example.petmatch.Animals.Domain.AnimalService;
import org.example.petmatch.Exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/animales")
public class AnimalController {


    @GetMapping
    public ResponseEntity<List<AnimalPresentationDTO>> getAllAnimals() throws ValidationException {
        List<AnimalPresentationDTO> animals = ;
    }
}
