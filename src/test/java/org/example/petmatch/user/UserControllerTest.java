package org.example.petmatch.user;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.petmatch.Animals.Domain.AnimalService;
import org.example.petmatch.User.Controller.UserController;
import org.example.petmatch.User.Dto.Request.UserLoginRequestDto;
import org.example.petmatch.User.Dto.Request.UserRegisterRequestDto;
import org.example.petmatch.User.Dto.Response.UserAuthResponseDto;
import org.example.petmatch.User.Exceptions.InvalidCredentialsException;
import org.example.petmatch.User.Exceptions.UserAlreadyExistsException;
import org.example.petmatch.User.Service.UserService;
import org.example.petmatch.Security.JwtAuthorizationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthorizationFilter.class
        ))
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AnimalService animalService;


    private UserRegisterRequestDto registerRequest;
    private UserLoginRequestDto loginRequest;
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
    @DisplayName("POST /user/auth/register - Should register user successfully")
    void shouldRegisterUserSuccessfully() throws Exception {
        when(userService.registerUser(any(UserRegisterRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.accessToken").value("jwt.token.here"));

        verify(userService, times(1)).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return 400 when email is invalid")
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        registerRequest.setEmail("invalid-email");

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return 400 when name is blank")
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        registerRequest.setName("");

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return 400 when lastname is blank")
    void shouldReturnBadRequestWhenLastnameIsBlank() throws Exception {
        registerRequest.setLastname("");

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return 400 when password is blank")
    void shouldReturnBadRequestWhenPasswordIsBlank() throws Exception {
        registerRequest.setPassword("");

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return 400 when password is weak")
    void shouldReturnBadRequestWhenPasswordIsWeak() throws Exception {
        registerRequest.setPassword("weak");

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return 400 when email is blank")
    void shouldReturnBadRequestWhenEmailIsBlank() throws Exception {
        registerRequest.setEmail("");

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should handle UserAlreadyExistsException")
    void shouldHandleUserAlreadyExistsException() throws Exception {
        when(userService.registerUser(any(UserRegisterRequestDto.class)))
                .thenThrow(new UserAlreadyExistsException("El usuario con email john@example.com ya existe"));

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().is4xxClientError());

        verify(userService, times(1)).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/login - Should login user successfully")
    void shouldLoginUserSuccessfully() throws Exception {
        when(userService.loginUser(any(UserLoginRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.accessToken").value("jwt.token.here"));

        verify(userService, times(1)).loginUser(any(UserLoginRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/login - Should return 400 when email is invalid")
    void shouldReturnBadRequestWhenLoginEmailIsInvalid() throws Exception {
        loginRequest.setEmail("invalid-email");

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).loginUser(any(UserLoginRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/login - Should return 400 when email is blank")
    void shouldReturnBadRequestWhenLoginEmailIsBlank() throws Exception {
        loginRequest.setEmail("");

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).loginUser(any(UserLoginRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/login - Should return 400 when password is blank")
    void shouldReturnBadRequestWhenLoginPasswordIsBlank() throws Exception {
        loginRequest.setPassword("");

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).loginUser(any(UserLoginRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/login - Should handle InvalidCredentialsException")
    void shouldHandleInvalidCredentialsException() throws Exception {
        when(userService.loginUser(any(UserLoginRequestDto.class)))
                .thenThrow(new InvalidCredentialsException("Credenciales inválidas"));

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is4xxClientError());

        verify(userService, times(1)).loginUser(any(UserLoginRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should accept valid password with all requirements")
    void shouldAcceptValidPasswordWithAllRequirements() throws Exception {
        registerRequest.setPassword("ValidPass123!");
        when(userService.registerUser(any(UserRegisterRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/login - Should return OK status code")
    void shouldReturnOkStatusCodeOnLogin() throws Exception {
        when(userService.loginUser(any(UserLoginRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        verify(userService, times(1)).loginUser(any(UserLoginRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return CREATED status code")
    void shouldReturnCreatedStatusCodeOnRegister() throws Exception {
        when(userService.registerUser(any(UserRegisterRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return 400 when name is null")
    void shouldReturnBadRequestWhenNameIsNull() throws Exception {
        registerRequest.setName(null);

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return 400 when lastname is null")
    void shouldReturnBadRequestWhenLastnameIsNull() throws Exception {
        registerRequest.setLastname(null);

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return 400 when email is null")
    void shouldReturnBadRequestWhenEmailIsNull() throws Exception {
        registerRequest.setEmail(null);

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should return 400 when password is null")
    void shouldReturnBadRequestWhenPasswordIsNull() throws Exception {
        registerRequest.setPassword(null);

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/login - Should return 400 when email is null")
    void shouldReturnBadRequestWhenLoginEmailIsNull() throws Exception {
        loginRequest.setEmail(null);

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).loginUser(any(UserLoginRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/login - Should return 400 when password is null")
    void shouldReturnBadRequestWhenLoginPasswordIsNull() throws Exception {
        loginRequest.setPassword(null);

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).loginUser(any(UserLoginRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/register - Should verify response contains all user fields")
    void shouldVerifyResponseContainsAllUserFields() throws Exception {
        when(userService.registerUser(any(UserRegisterRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.lastname").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.accessToken").exists());

        verify(userService, times(1)).registerUser(any(UserRegisterRequestDto.class));
    }

    @Test
    @DisplayName("POST /user/auth/login - Should verify response contains all user fields")
    void shouldVerifyLoginResponseContainsAllUserFields() throws Exception {
        when(userService.loginUser(any(UserLoginRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.lastname").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.accessToken").exists());

        verify(userService, times(1)).loginUser(any(UserLoginRequestDto.class));
    }
}
