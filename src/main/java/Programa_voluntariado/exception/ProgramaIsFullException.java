package Programa_voluntariado.exception;

public class ProgramaIsFullException extends RuntimeException {
    public ProgramaIsFullException(String message) {
        super(message);
    }
}
