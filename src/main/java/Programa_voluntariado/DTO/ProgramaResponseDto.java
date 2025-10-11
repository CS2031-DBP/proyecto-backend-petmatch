package Programa_voluntariado.DTO;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ProgramaResponseDto {
    public String nombre;
    public String descripcion;
    public ZonedDateTime fechaInicio;
    public ZonedDateTime fechaFin;
    public String ubicacion;
    public Integer numeroVoluntariosNecesarios;
    public Integer numeroVoluntariosInscritos;
    public String status;
}
