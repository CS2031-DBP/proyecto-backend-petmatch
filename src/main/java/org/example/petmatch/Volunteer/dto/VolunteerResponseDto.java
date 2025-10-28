package org.example.petmatch.Volunteer.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VolunteerResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
}
