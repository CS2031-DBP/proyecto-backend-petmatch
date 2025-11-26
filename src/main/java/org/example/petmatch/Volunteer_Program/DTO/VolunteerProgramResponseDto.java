package org.example.petmatch.Volunteer_Program.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Data
public class VolunteerProgramResponseDto {
    private Long id;
    private String name; // ✅ CAMBIADO
    private String description; // ✅ CAMBIADO
    private ZonedDateTime startDate; // ✅ CAMBIADO
    private ZonedDateTime finishDate; // ✅ CAMBIADO
    private String location; // ✅ CAMBIADO de "ubicacion"
    private Integer necessaryVolunteers; // ✅ CAMBIADO
    private Integer enrolledVolunteers; // ✅ CAMBIADO de "numeroVoluntariosInscritos"
    private String status;
    private Long shelterId; // ✅ AGREGADO - útil para el frontend
    private String shelterName; // ✅ AGREGADO - útil para el frontend
}