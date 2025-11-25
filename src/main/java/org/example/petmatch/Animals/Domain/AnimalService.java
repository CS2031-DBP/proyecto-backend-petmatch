package org.example.petmatch.Animals.Domain;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Animals.DTO.AnimalPresentationDTO;
import org.example.petmatch.Animals.DTO.AnimalReportDTO;
import org.example.petmatch.Animals.Infraestructure.AnimalRepository;
import org.example.petmatch.Shelter.Domain.Shelter;
import org.example.petmatch.Shelter.Infraestructure.ShelterRepository;
import org.modelmapper.ModelMapper;
import org.example.petmatch.Exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;
    private final ModelMapper modelMapper;

    public AnimalPresentationDTO createReport(AnimalReportDTO instanceAnimal) throws ValidationException {

        if (instanceAnimal == null) {
            throw new ValidationException("Debes enviar las características del animal");
        }

        if (animalRepository.findByName(instanceAnimal.getName()).isPresent()) {
            throw new ValidationException("El animal ya existe");
        }

        Animal animal = modelMapper.map(instanceAnimal, Animal.class);
        animalRepository.save(animal);
        return modelMapper.map(animal, AnimalPresentationDTO.class);
    }
    public AnimalPresentationDTO assignShelterToAnimal(String animalName, String shelterName) throws ValidationException {
        Animal animal = animalRepository.findByName(animalName)
                .orElseThrow(() -> new ValidationException("No se encontró un animal con el nombre: " + animalName));

        Shelter shelter = shelterRepository.findByName(shelterName)
                .orElseThrow(() -> new ValidationException("No se encontró un albergue con el nombre: " + shelterName));

        animal.setShelter(shelter);
        animalRepository.save(animal);

        return modelMapper.map(animal, AnimalPresentationDTO.class);
    }


    public List<AnimalPresentationDTO> getAllAnimalsNoRegistered() {
        List<AnimalPresentationDTO> animals = new ArrayList<>();
        animalRepository.findAll().forEach(animal -> {
            if (!animal.getRegistered()) {
                animals.add(modelMapper.map(animal, AnimalPresentationDTO.class));
            }
        });
        return animals;
    }
}
