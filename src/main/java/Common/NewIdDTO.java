package Common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewIdDTO {
    private Long id;

    public NewIdDTO(Long id) {
        this.id = id;
    }
}