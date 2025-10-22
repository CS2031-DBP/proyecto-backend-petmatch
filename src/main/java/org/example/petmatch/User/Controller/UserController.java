package org.example.petmatch.User.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petmatch.User.Dto.Request.UserLoginRequestDto;
import org.example.petmatch.User.Dto.Request.UserRegisterRequestDto;
import org.example.petmatch.User.Dto.Response.UserAuthResponseDto;
import org.example.petmatch.User.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserAuthResponseDto> register(@Valid @RequestBody UserRegisterRequestDto request){
        UserAuthResponseDto responseDto = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponseDto> login(@Valid @RequestBody UserLoginRequestDto request){
        UserAuthResponseDto responseDto = userService.loginUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
