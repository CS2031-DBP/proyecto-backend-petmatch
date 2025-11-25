package org.example.petmatch.Post.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PostViewDTO {

    @NotNull
    private String title;

    @NotNull
    @NotEmpty
    private String necessities;

    @NotNull
    @NotEmpty
    private String shelterName;

}
