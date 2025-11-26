package org.example.petmatch.user;

import org.example.petmatch.Email.EmailService;
import org.example.petmatch.Security.JwtService;
import org.example.petmatch.User.Domain.Role;
import org.example.petmatch.User.Domain.User;
import org.example.petmatch.User.Dto.Request.UserLoginRequestDto;
import org.example.petmatch.User.Dto.Request.UserRegisterRequestDto;
import org.example.petmatch.User.Dto.Response.UserAuthResponseDto;
import org.example.petmatch.User.Exceptions.InvalidCredentialsException;
import org.example.petmatch.User.Exceptions.UserAlreadyExistsException;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.example.petmatch.User.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    private UserRegisterRequestDto registerRequest;
    private UserLoginRequestDto loginRequest;
    private User user;
    private UserAuthResponseDto authResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new UserRegisterRequestDto();
        registerRequest.setName("John");
        registerRequest.setLastname("Doe");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("Password123!");

        loginRequest = new UserLoginRequestDto();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("Password123!");

        user = User.builder()
                .id(1L)
                .name("John")
                .lastname("Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        authResponse = UserAuthResponseDto.builder()
                .id(1L)
                .name("John")
                .lastname("Doe")
                .email("john@example.com")
                .role("USER")
                .accessToken("jwt.token.here")
                .build();
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(registerRequest, User.class)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("jwt.token.here");
        when(modelMapper.map(user, UserAuthResponseDto.class)).thenReturn(authResponse);
        doNothing().when(emailService).sendWelcomeEmailUser(anyString(), anyString());

        UserAuthResponseDto result = userService.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("USER", result.getRole());
        assertEquals("jwt.token.here", result.getAccessToken());

        verify(userRepository).existsByEmail("john@example.com");
        verify(passwordEncoder).encode("Password123!");
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken("john@example.com", "USER");
        verify(emailService).sendWelcomeEmailUser("john@example.com", "John");
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email exists")
    void shouldThrowExceptionWhenUserAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.registerUser(registerRequest)
        );

        assertTrue(exception.getMessage().contains("john@example.com"));
        assertTrue(exception.getMessage().contains("ya existe"));

        verify(userRepository).existsByEmail("john@example.com");
        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendWelcomeEmailUser(anyString(), anyString());
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("jwt.token.here");
        when(modelMapper.map(user, UserAuthResponseDto.class)).thenReturn(authResponse);

        UserAuthResponseDto result = userService.loginUser(loginRequest);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("USER", result.getRole());
        assertEquals("jwt.token.here", result.getAccessToken());

        verify(userRepository).findByEmail("john@example.com");
        verify(passwordEncoder).matches("Password123!", "encodedPassword");
        verify(jwtService).generateToken("john@example.com", "USER");
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when user not found")
    void shouldThrowExceptionWhenUserNotFoundDuringLogin() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> userService.loginUser(loginRequest)
        );

        assertTrue(exception.getMessage().contains("inválidas") ||
                exception.getMessage().contains("invÃ¡lidas"));

        verify(userRepository).findByEmail("john@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password is incorrect")
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> userService.loginUser(loginRequest)
        );

        assertTrue(exception.getMessage().contains("inválidas") ||
                exception.getMessage().contains("invÃ¡lidas"));

        verify(userRepository).findByEmail("john@example.com");
        verify(passwordEncoder).matches("Password123!", "encodedPassword");
        verify(jwtService, never()).generateToken(anyString(), anyString());
    }

    @Test
    @DisplayName("Should set USER role when registering")
    void shouldSetUserRoleWhenRegistering() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(registerRequest, User.class)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals(Role.USER, savedUser.getRole());
            return savedUser;
        });
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("jwt.token.here");
        when(modelMapper.map(user, UserAuthResponseDto.class)).thenReturn(authResponse);
        doNothing().when(emailService).sendWelcomeEmailUser(anyString(), anyString());

        userService.registerUser(registerRequest);

        verify(userRepository).save(argThat(u -> u.getRole() == Role.USER));
    }

    @Test
    @DisplayName("Should encode password before saving")
    void shouldEncodePasswordBeforeSaving() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(registerRequest, User.class)).thenReturn(user);
        when(passwordEncoder.encode("Password123!")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("jwt.token.here");
        when(modelMapper.map(user, UserAuthResponseDto.class)).thenReturn(authResponse);
        doNothing().when(emailService).sendWelcomeEmailUser(anyString(), anyString());

        userService.registerUser(registerRequest);

        verify(passwordEncoder).encode("Password123!");
        verify(userRepository).save(argThat(u -> u.getPassword().equals("encodedPassword")));
    }

    @Test
    @DisplayName("Should generate JWT token with USER role on registration")
    void shouldGenerateJwtTokenWithUserRoleOnRegistration() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(registerRequest, User.class)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken("john@example.com", "USER")).thenReturn("jwt.token.here");
        when(modelMapper.map(user, UserAuthResponseDto.class)).thenReturn(authResponse);
        doNothing().when(emailService).sendWelcomeEmailUser(anyString(), anyString());

        UserAuthResponseDto result = userService.registerUser(registerRequest);

        assertEquals("jwt.token.here", result.getAccessToken());
        verify(jwtService).generateToken("john@example.com", "USER");
    }

    @Test
    @DisplayName("Should generate JWT token with USER role on login")
    void shouldGenerateJwtTokenWithUserRoleOnLogin() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken("john@example.com", "USER")).thenReturn("jwt.token.here");
        when(modelMapper.map(user, UserAuthResponseDto.class)).thenReturn(authResponse);

        UserAuthResponseDto result = userService.loginUser(loginRequest);

        assertEquals("jwt.token.here", result.getAccessToken());
        verify(jwtService).generateToken("john@example.com", "USER");
    }

    @Test
    @DisplayName("Should send welcome email after registration")
    void shouldSendWelcomeEmailAfterRegistration() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(registerRequest, User.class)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("jwt.token.here");
        when(modelMapper.map(user, UserAuthResponseDto.class)).thenReturn(authResponse);
        doNothing().when(emailService).sendWelcomeEmailUser(anyString(), anyString());

        userService.registerUser(registerRequest);

        verify(emailService).sendWelcomeEmailUser("john@example.com", "John");
    }

    @Test
    @DisplayName("Should map user data correctly in response")
    void shouldMapUserDataCorrectlyInResponse() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(registerRequest, User.class)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("jwt.token.here");
        when(modelMapper.map(user, UserAuthResponseDto.class)).thenReturn(authResponse);
        doNothing().when(emailService).sendWelcomeEmailUser(anyString(), anyString());

        UserAuthResponseDto result = userService.registerUser(registerRequest);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getLastname());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    @DisplayName("Should verify all dependencies are called in correct order during registration")
    void shouldVerifyDependenciesCalledInOrderDuringRegistration() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(registerRequest, User.class)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("jwt.token.here");
        when(modelMapper.map(user, UserAuthResponseDto.class)).thenReturn(authResponse);
        doNothing().when(emailService).sendWelcomeEmailUser(anyString(), anyString());

        userService.registerUser(registerRequest);

        var inOrder = inOrder(userRepository, passwordEncoder, jwtService, emailService);
        inOrder.verify(userRepository).existsByEmail(anyString());
        inOrder.verify(passwordEncoder).encode(anyString());
        inOrder.verify(userRepository).save(any(User.class));
        inOrder.verify(jwtService).generateToken(anyString(), anyString());
        inOrder.verify(emailService).sendWelcomeEmailUser(anyString(), anyString());
    }
}
