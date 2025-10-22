package org.example.petmatch.Comentarios.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComentarioRequestDTO {

    @NotNull
    @NotEmpty
    private String content;

    @NotNull
    @NotEmpty
    private String post_name;

    @NotNull
    @NotEmpty
    private String user_name;
}
