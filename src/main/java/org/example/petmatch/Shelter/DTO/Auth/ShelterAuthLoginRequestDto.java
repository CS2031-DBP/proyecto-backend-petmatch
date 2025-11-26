package org.example.petmatch.Shelter.DTO.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShelterAuthLoginRequestDto {
    @Email(message = "ERROR: El correo no tiene un formato válido")
    @NotBlank(message = "ERROR: Debe ingresar un correo")
    private String email;

    @NotBlank(message = "ERROR: Debe ingresar una contraseña")
    private String password;
}
