package org.example.petmatch.Comments.DTO;

import lombok.Data;

@Data
public class CommentsResponseDTO {

    private String content;
    private String post_name;
    private String user_name;

}
