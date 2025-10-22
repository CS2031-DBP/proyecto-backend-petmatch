package org.example.petmatch.Programa_voluntariado.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ProgramaRequestDto {
    @NotEmpty
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotEmpty
    @Size(min = 10, max = 1000, message = "La descripcion debe tener entre 10 y 1000 caracteres")
    private String descripcion;

    @Future
    @NotEmpty
    private ZonedDateTime fechaInicio;

    @NotEmpty
    @Future
    private ZonedDateTime fechaFin;

    @NotEmpty
    @Max(100)
    @Min(1)
    private Integer numeroVoluntariosNecesarios;

    @AssertTrue
    public boolean isFechaFinAfterFechaInicio() {
        if (fechaInicio == null || fechaFin == null) {
            return true;
        }
        return fechaFin.isAfter(fechaInicio);
    }
}
