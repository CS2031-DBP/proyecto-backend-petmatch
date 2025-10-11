package Albergue.DTO;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private Integer phone;
}
