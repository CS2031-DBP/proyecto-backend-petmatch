package Voluntario.exception;

public class VoluntarioNotFoundException extends RuntimeException {
    public VoluntarioNotFoundException(String message) {
        super(message);
    }
}
