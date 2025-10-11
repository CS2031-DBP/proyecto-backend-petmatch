package Post.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PostViewDTO {

    @NotNull
    @NotNull
    private Long title;

    @NotNull
    @NotEmpty
    private List<String> necessities;

    @NotNull
    @NotEmpty
    private String albergue_name;

}
