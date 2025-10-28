package org.example.petmatch.Shelter.DTO.Auth;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ShelterAuthResponseDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private Long phone;
    private Integer capacity;
    private Integer availableSpaces;
    private String email;
    private String rating;
    private String accessToken;
}
