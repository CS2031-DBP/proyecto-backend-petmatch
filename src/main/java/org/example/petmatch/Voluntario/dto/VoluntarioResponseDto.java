package org.example.petmatch.Voluntario.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VoluntarioResponseDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
}
