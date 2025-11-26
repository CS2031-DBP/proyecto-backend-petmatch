package org.example.petmatch.Shelter.Exceptions;

public class ShelterAlreadyExistsException extends RuntimeException {
    public ShelterAlreadyExistsException(String message) {
        super(message);
    }
}
