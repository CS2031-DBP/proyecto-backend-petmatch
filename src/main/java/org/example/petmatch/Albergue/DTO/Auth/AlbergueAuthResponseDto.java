package org.example.petmatch.Albergue.DTO.Auth;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AlbergueAuthResponseDto {
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
