package org.example.petmatch.User.Dto.Response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String role;
    private String accessToken;

}
