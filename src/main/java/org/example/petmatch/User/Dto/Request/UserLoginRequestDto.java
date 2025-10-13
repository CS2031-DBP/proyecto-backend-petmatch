package org.example.petmatch.User.Dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {

    @Email(message = "ERROR: El correo no tiene un formato valido")
    @NotBlank(message = "ERROR: Debe de ingresar un correo")
    private String email;

    @NotBlank(message = "ERROR: Debe de ingresar una contraseña")
    private String password;

}
