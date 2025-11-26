package org.example.petmatch.Volunteer_Program.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.ZonedDateTime;
@Data
public class VolunteerProgramRequestDto {
    @NotEmpty
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name; // ✅ CAMBIADO de "nombre" a "name"

    @NotEmpty
    @Size(min = 10, max = 1000, message = "La descripcion debe tener entre 10 y 1000 caracteres")
    private String description; // ✅ CAMBIADO de "descripcion" a "description"

    @Future
    @NotNull
    private ZonedDateTime startDate; // ✅ CAMBIADO de "fechaInicio" a "startDate"

    @NotNull
    @Future
    private ZonedDateTime finishDate; // ✅ CAMBIADO de "fechaFin" a "finishDate"

    @NotNull
    @Max(100)
    @Min(1)
    private Integer necessaryVolunteers; // ✅ CAMBIADO de "numeroVoluntariosNecesarios"

    @AssertTrue(message = "La fecha de fin debe ser posterior a la fecha de inicio")
    public boolean isFinishDateAfterStartDate() {
        if (startDate == null || finishDate == null) {
            return true;
        }
        return finishDate.isAfter(startDate);
    }
}