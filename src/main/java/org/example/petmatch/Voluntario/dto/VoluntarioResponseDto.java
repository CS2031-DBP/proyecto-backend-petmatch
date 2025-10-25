package org.example.petmatch.Voluntario.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VoluntarioResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
}
