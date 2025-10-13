package org.example.petmatch.User.Service;


import org.example.petmatch.Security.JwtService;
import org.example.petmatch.User.Domain.User;
import org.example.petmatch.User.Domain.Role;
import org.example.petmatch.User.Dto.Request.UserLoginRequestDto;
import org.example.petmatch.User.Dto.Request.UserRegisterRequestDto;
import org.example.petmatch.User.Dto.Response.UserAuthResponseDto;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Transactional
    public UserAuthResponseDto registerUser(UserRegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email ya registrado");
        }

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), "USER");

        UserAuthResponseDto responseDto = modelMapper.map(user,UserAuthResponseDto.class);
        responseDto.setRole(user.getRole().toString());
        responseDto.setAccessToken(token);

        return responseDto;
    }

    @Transactional(readOnly = true)
    public UserAuthResponseDto loginUser(UserLoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user.getEmail(), "USER");

        UserAuthResponseDto response = modelMapper.map(user, UserAuthResponseDto.class);
        response.setRole(user.getRole().toString());
        response.setAccessToken(token);

        return response;
    }



}
