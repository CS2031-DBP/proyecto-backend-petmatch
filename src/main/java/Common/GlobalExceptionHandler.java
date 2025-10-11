package Common;

import Programa_voluntariado.exception.ProgramaIsFullException;
import Programa_voluntariado.exception.ProgramaNotFoundException;
import VoluntarioPrograma.exception.AlreadyEnrolledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ProgramaNotFoundException.class)
    public ResponseEntity<String> handleProgramaNotFound(ProgramaNotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(AlreadyEnrolledException.class)
    public ResponseEntity<String> handleAlreadyEnrolled(AlreadyEnrolledException e){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(ProgramaIsFullException.class)
    public ResponseEntity<String> handleProgramaIsFull(ProgramaIsFullException e){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

}
