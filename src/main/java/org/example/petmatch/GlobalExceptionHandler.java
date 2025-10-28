package org.example.petmatch;

import org.example.petmatch.Shelter.Exceptions.ShelterAlreadyExistsException;
import org.example.petmatch.Shelter.Exceptions.ShelterNotFoundException;
import org.example.petmatch.Shelter.Exceptions.ValidationException;
import org.example.petmatch.Exception.NotFoundException;
import org.example.petmatch.Inscription.exception.AlreadyEnrolledException;
import org.example.petmatch.Inscription.exception.InscriptionNotFoundException;
import org.example.petmatch.Volunteer_Program.exception.VolunteerProgramIsFullException;
import org.example.petmatch.Volunteer_Program.exception.VolunteerProgramNotFoundException;
import org.example.petmatch.User.Exceptions.InvalidCredentialsException;
import org.example.petmatch.User.Exceptions.UserAlreadyExistsException;
import org.example.petmatch.User.Exceptions.UserNotFoundException;
import org.example.petmatch.Volunteer.exception.VolunteerNotFoundException;
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

    @ExceptionHandler(ShelterAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAlbergueAlreadyExistsException(
            ShelterAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "El albergue ya se encuentra registrado", ex.getMessage());
    }

    @ExceptionHandler(ShelterNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAlbergueNotFoundException(
            ShelterNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Albergue no encontardo", ex.getMessage());
    }

    @ExceptionHandler(AlreadyEnrolledException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyEnrolledException(
            AlreadyEnrolledException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "El usuario ya se encuentra inscripto en el programa", ex.getMessage());
    }

    @ExceptionHandler(InscriptionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleInscripcionNotFoundException(
            InscriptionNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Inscripcion no encontrada", ex.getMessage());
    }

    @ExceptionHandler(VolunteerProgramIsFullException.class)
    public ResponseEntity<Map<String, Object>> handleProgramaIsFullException(
            VolunteerProgramIsFullException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "El programa ya ha alcanzado el número máximo de voluntarios", ex.getMessage());
    }

    @ExceptionHandler(VolunteerProgramNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProgramaNotFoundException(
            VolunteerProgramNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Programa no encontrado", ex.getMessage());
    }

    @ExceptionHandler(VolunteerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVoluntarioNotFoundException(
            VolunteerNotFoundException ex) {
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