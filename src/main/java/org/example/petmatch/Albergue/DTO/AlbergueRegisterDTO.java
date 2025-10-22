package org.example.petmatch.Albergue.DTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class AlbergueRegisterDTO {
    @NonNull
    @NotEmpty
    private String password;

    @NonNull
    @NotEmpty
    @Pattern(regexp = "^[A-Z].+")
    private String name;

    @NonNull
    @NotEmpty
    private String description;

    @NonNull
    @NotEmpty
    private String address;

    @NonNull
    @Pattern(
            regexp = "^(\\+?51)?9\\d{8}$",
            message = "Los números válidos deben tener el formato: +519XXXXXXXX or 9XXXXXXXX)"
    )
    private Long phone;

    @NonNull
    @NotEmpty
    @Min(1)
    private Integer capacity;

    @NonNull
    @NotEmpty
    @Email
    private String email;

}
