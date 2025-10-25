package org.example.petmatch.Programa_voluntariado.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Data
public class ProgramaResponseDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private ZonedDateTime fechaInicio;
    private ZonedDateTime fechaFin;
    private String ubicacion;
    private Integer numeroVoluntariosNecesarios;
    private Integer numeroVoluntariosInscritos;
    private String status;
}
