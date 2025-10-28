package org.example.petmatch.Shelter.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.example.petmatch.Shelter.Domain.Rating;

@Getter
@Setter
public class RequestShelterDTO {
    @NonNull
    @NotEmpty
    private String name;

    @NonNull
    @NotEmpty
    private Integer phone;

    @NonNull
    @NotEmpty
    @Min(1)
    private String availableSpaces;

    @NonNull
    @NotEmpty
    private Integer capacity;

    @NonNull
    @NotEmpty
    private String address;

    @NonNull
    @NotEmpty
    private Rating rating;


}