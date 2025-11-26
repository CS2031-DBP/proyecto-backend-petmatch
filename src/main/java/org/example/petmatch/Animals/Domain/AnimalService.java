package org.example.petmatch.Animals.Domain;

import jakarta.transaction.Transactional;
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
        animal.setRegistered(false);  // Asegurar que inicia como no registrado
        animalRepository.save(animal);

        AnimalPresentationDTO dto = modelMapper.map(animal, AnimalPresentationDTO.class);
        dto.setShelterName(null);
        return dto;
    }

    @Transactional
    public AnimalPresentationDTO assignShelterToAnimal(String animalName, String shelterName) throws ValidationException {
        Animal animal = animalRepository.findByName(animalName)
                .orElseThrow(() -> new ValidationException("No se encontró un animal con el nombre: " + animalName));

        // ⭐ VALIDAR: Si ya está registrado, no se puede asignar de nuevo
        if (animal.getRegistered()) {
            throw new ValidationException("Este animal ya está registrado en un albergue");
        }

        // Buscar el albergue
        Shelter shelter = shelterRepository.findByName(shelterName)
                .orElseThrow(() -> new ValidationException("No se encontró un albergue con el nombre: " + shelterName));

        // ⭐ VALIDAR: Si el albergue tiene espacios disponibles
        if (shelter.getAvailableSpaces() <= 0) {
            throw new ValidationException("El albergue no tiene espacios disponibles");
        }

        // ⭐ Asignar el animal al albergue
        animal.setShelter(shelter);
        animal.setRegistered(true);  // ← MARCAR COMO REGISTRADO

        // ⭐ Reducir espacios disponibles del albergue
        shelter.setAvailableSpaces(shelter.getAvailableSpaces() - 1);

        // Guardar cambios
        animalRepository.save(animal);
        shelterRepository.save(shelter);

        // Crear el DTO de respuesta
        AnimalPresentationDTO dto = modelMapper.map(animal, AnimalPresentationDTO.class);
        dto.setShelterName(shelter.getName());
        return dto;
    }

    public List<AnimalPresentationDTO> getAllAnimalsNoRegistered() {
        List<AnimalPresentationDTO> animals = new ArrayList<>();
        animalRepository.findAll().forEach(animal -> {
            if (!animal.getRegistered()) {  // Solo los NO registrados
                AnimalPresentationDTO dto = modelMapper.map(animal, AnimalPresentationDTO.class);
                dto.setShelterName(null);  // No tienen albergue asignado
                animals.add(dto);
            }
        });
        return animals;
    }
}
