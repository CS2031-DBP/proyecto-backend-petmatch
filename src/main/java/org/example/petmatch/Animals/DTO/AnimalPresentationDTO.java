package org.example.petmatch.Animals.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnimalPresentationDTO {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String breed;

    @NotNull
    @NotEmpty
    private String image;

    @NotNull
    private Boolean registered;

}
