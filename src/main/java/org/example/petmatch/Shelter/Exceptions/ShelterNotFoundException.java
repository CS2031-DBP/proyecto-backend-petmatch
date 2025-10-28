package org.example.petmatch.Shelter.Exceptions;

public class ShelterNotFoundException extends RuntimeException {
    public ShelterNotFoundException(String message) {
        super(message);
    }
}
