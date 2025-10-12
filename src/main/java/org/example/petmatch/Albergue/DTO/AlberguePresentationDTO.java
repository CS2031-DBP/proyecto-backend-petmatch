package org.example.petmatch.Albergue.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class AlberguePresentationDTO {

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
    private Long phone;
    @NonNull
    @NotEmpty
    private Integer avaliableSpaces;
}