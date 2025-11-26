package org.example.petmatch.Shelter.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ShelterPresentationDTO {

    @NonNull
    @NotEmpty
    private String name;
    @NonNull
    @NotEmpty
    private String address;
    @NonNull
    @Pattern(
            regexp = "^(\\+?51)?9\\d{8}$",
            message = "Los números válidos deben tener el formato: +519XXXXXXXX or 9XXXXXXXX)"
    )
    private String phone;
    @NonNull
    @NotEmpty
    private Integer availableSpaces;
}