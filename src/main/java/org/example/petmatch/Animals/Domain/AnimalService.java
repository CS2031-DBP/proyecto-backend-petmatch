package org.example.petmatch.Animals.Domain;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Animals.DTO.AnimalPresentationDTO;
import org.example.petmatch.Animals.DTO.AnimalReportDTO;
import org.example.petmatch.Animals.Infraestructure.AnimalRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;

    public AnimalPresentationDTO createReport(AnimalReportDTO instanceAnimal) {
        Animal animal = modelMapper.map(instanceAnimal, Animal.class);
        animalRepository.save(animal);
        return modelMapper.map(animal, AnimalPresentationDTO.class);
    }


}
