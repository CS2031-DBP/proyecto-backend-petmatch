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
import org.example.petmatch.Volunteer.Domain.Volunteer;
import org.example.petmatch.Volunteer.Infraestructure.VolunteerRepository;
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
    private final VolunteerRepository volunteerRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Transactional
    public UserAuthResponseDto registerUser(UserRegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("El usuario con email " + request.getEmail() + " ya existe");
        }

        // ✅ CAMBIADO: Crear Volunteer en lugar de User
        // Como Volunteer extends User, esto creará registros en AMBAS tablas
        Volunteer volunteer = new Volunteer();
        volunteer.setName(request.getName());
        volunteer.setLastname(request.getLastname());
        volunteer.setEmail(request.getEmail());
        volunteer.setPassword(passwordEncoder.encode(request.getPassword()));
        volunteer.setRole(Role.USER);

        // Esto creará el registro en la tabla 'users' Y en la tabla 'volunteers'
        volunteerRepository.save(volunteer);

        String token = jwtService.generateToken(volunteer.getEmail(), "USER");

        emailService.sendWelcomeEmailUser(volunteer.getEmail(), volunteer.getName());

        UserAuthResponseDto responseDto = new UserAuthResponseDto();
        responseDto.setId(volunteer.getId());
        responseDto.setName(volunteer.getName());
        responseDto.setLastname(volunteer.getLastname());
        responseDto.setEmail(volunteer.getEmail());
        responseDto.setRole(volunteer.getRole().toString());
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
