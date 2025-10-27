package org.example.petmatch;

import org.example.petmatch.Albergue.Exceptions.AlbergueAlreadyExistsException;
import org.example.petmatch.Albergue.Exceptions.AlbergueNotFoundException;
import org.example.petmatch.Albergue.Exceptions.ValidationException;
import org.example.petmatch.Exception.NotFoundException;
import org.example.petmatch.Inscripcion.exception.AlreadyEnrolledException;
import org.example.petmatch.Inscripcion.exception.InscripcionNotFoundException;
import org.example.petmatch.Programa_voluntariado.exception.ProgramaIsFullException;
import org.example.petmatch.Programa_voluntariado.exception.ProgramaNotFoundException;
import org.example.petmatch.User.Exceptions.InvalidCredentialsException;
import org.example.petmatch.User.Exceptions.UserAlreadyExistsException;
import org.example.petmatch.User.Exceptions.UserNotFoundException;
import org.example.petmatch.Voluntario.exception.VoluntarioNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Solicitud no valida", ex.getMessage());
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return buildErrorResponse(HttpStatus.NOT_ACCEPTABLE, "Credenciales invalidas", ex.getMessage());
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAccessException(ResourceAccessException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND,  "El recurso no fue encontrado", ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT,  "El usuario ya se encuentra registrado", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND,  "El usuario ya se encuentra registrado", ex.getMessage());
    }

    @ExceptionHandler(AlbergueAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAlbergueAlreadyExistsException(
            AlbergueAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "El albergue ya se encuentra registrado", ex.getMessage());
    }

    @ExceptionHandler(AlbergueNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAlbergueNotFoundException(
            AlbergueNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Albergue no encontardo", ex.getMessage());
    }

    @ExceptionHandler(AlreadyEnrolledException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyEnrolledException(
            AlreadyEnrolledException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "El usuario ya se encuentra inscripto en el programa", ex.getMessage());
    }

    @ExceptionHandler(InscripcionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleInscripcionNotFoundException(
            InscripcionNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Inscripcion no encontrada", ex.getMessage());
    }

    @ExceptionHandler(ProgramaIsFullException.class)
    public ResponseEntity<Map<String, Object>> handleProgramaIsFullException(
            ProgramaIsFullException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "El programa ya ha alcanzado el número máximo de voluntarios", ex.getMessage());
    }

    @ExceptionHandler(ProgramaNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProgramaNotFoundException(
            ProgramaNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Programa no encontrado", ex.getMessage());
    }

    @ExceptionHandler(VoluntarioNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVoluntarioNotFoundException(
            VoluntarioNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Voluntario no encontrado", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }

}