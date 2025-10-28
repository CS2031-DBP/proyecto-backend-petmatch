package org.example.petmatch.User.Service;


import org.example.petmatch.Email.EmailService;
import org.example.petmatch.Security.JwtService;
import org.example.petmatch.User.Domain.User;
import org.example.petmatch.User.Domain.Role;
import org.example.petmatch.User.Dto.Request.UserLoginRequestDto;
import org.example.petmatch.User.Dto.Request.UserRegisterRequestDto;
import org.example.petmatch.User.Dto.Response.UserAuthResponseDto;
import org.example.petmatch.User.Exceptions.InvalidCredentialsException;
import org.example.petmatch.User.Exceptions.UserAlreadyExistsException;
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
    private final EmailService emailService;

    @Transactional
    public UserAuthResponseDto registerUser(UserRegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("El usuario con email " + request.getEmail() + " ya existe");
        }

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), "USER");

        emailService.sendWelcomeEmailUser(user.getEmail(), user.getName());

        UserAuthResponseDto responseDto = modelMapper.map(user,UserAuthResponseDto.class);
        responseDto.setRole(user.getRole().toString());
        responseDto.setAccessToken(token);

        return responseDto;
    }

    @Transactional(readOnly = true)
    public UserAuthResponseDto loginUser(UserLoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user.getEmail(), "USER");

        UserAuthResponseDto response = modelMapper.map(user, UserAuthResponseDto.class);
        response.setRole(user.getRole().toString());
        response.setAccessToken(token);

        return response;
    }



}
