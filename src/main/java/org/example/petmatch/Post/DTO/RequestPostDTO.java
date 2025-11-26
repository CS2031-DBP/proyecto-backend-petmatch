package org.example.petmatch.Post.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestPostDTO {

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private String necessities;

}
