package org.example.petmatch.Albergue.Exceptions;

public class AlbergueAlreadyExistsException extends RuntimeException {
    public AlbergueAlreadyExistsException(String message) {
        super(message);
    }
}
