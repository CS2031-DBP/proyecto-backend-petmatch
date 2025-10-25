package org.example.petmatch.Comentarios.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComentarioResponseDTO {

    private String content;
    private String post_name;
    private String user_name;

}
