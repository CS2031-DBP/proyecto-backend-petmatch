package org.example.petmatch.User.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String role;
    private String accessToken;

}
