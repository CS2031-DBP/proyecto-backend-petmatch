package org.example.petmatch.Programa_voluntariado.DTO;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ProgramaResponseDto {
    private String nombre;
    private String descripcion;
    private ZonedDateTime fechaInicio;
    private ZonedDateTime fechaFin;
    private String ubicacion;
    private Integer numeroVoluntariosNecesarios;
    private Integer numeroVoluntariosInscritos;
    private String status;
}
