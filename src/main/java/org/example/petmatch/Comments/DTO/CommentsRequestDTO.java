package org.example.petmatch.Comments.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentsRequestDTO {

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
