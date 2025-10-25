package org.example.petmatch.Albergue.DTO.Auth;
import org.example.petmatch.User.Validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlbergueAuthRegisterRequestDto {
    @NotBlank(message = "ERROR: Debe ingresar un nombre")
    private String name;

    @NotBlank(message = "ERROR: Debe ingresar una dirección")
    private String address;

    @NotNull(message = "ERROR: Debe ingresar un teléfono")
    private Long phone;

    @NotNull(message = "ERROR: Debe ingresar la capacidad")
    @Min(value = 1, message = "ERROR: La capacidad debe ser mayor a 0")
    private Integer capacity;

    @Email(message = "ERROR: El correo no tiene un formato válido")
    @NotBlank(message = "ERROR: Debe ingresar un correo")
    private String email;

    @NotBlank(message = "ERROR: Debe ingresar una contraseña")
    @ValidPassword
    private String password;
}
