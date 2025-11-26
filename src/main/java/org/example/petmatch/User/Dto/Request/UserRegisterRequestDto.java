package org.example.petmatch.User.Dto.Request;

import org.example.petmatch.User.Validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Data
public class UserRegisterRequestDto {

    @NotBlank(message = "ERROR: Debe de ingresar un nombre")
    private String name;

    @NotBlank(message = "ERROR: Debe de ingresar un appellido")
    private String lastname;

    @Email(message = "ERROR: El correo no tiene un formato valido")
    @NotBlank(message = "ERROR: Debe de ingresar un correo")
    private String email;

    @NotBlank(message = "ERROR: Debe de ingresar una contraseña")
    @ValidPassword
    private String password;

}
