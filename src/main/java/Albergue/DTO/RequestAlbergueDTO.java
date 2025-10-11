package Albergue.DTO;

import Albergue.Domain.Rating;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class RequestAlbergueDTO {
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
