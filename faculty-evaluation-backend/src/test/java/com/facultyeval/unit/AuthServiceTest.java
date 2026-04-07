package com.facultyeval.unit;

import com.facultyeval.config.JwtUtil;
import com.facultyeval.dto.LoginRequest;
import com.facultyeval.dto.LoginResponse;
import com.facultyeval.exception.BadRequestException;
import com.facultyeval.model.Role;
import com.facultyeval.model.User;
import com.facultyeval.repository.FacultyRepository;
import com.facultyeval.repository.StudentRepository;
import com.facultyeval.repository.UserRepository;
import com.facultyeval.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for AuthService
 * Tests login logic, credential validation, and response building.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;
    @Mock private FacultyRepository facultyRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private JwtUtil jwtUtil;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User adminUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .id(1L)
                .username("admin")
                .password("encodedPassword")
                .fullName("System Admin")
                .email("admin@fes.edu")
                .role(Role.ADMIN)
                .active(true)
                .build();

        loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("Admin@123");
    }

    @Test
    @DisplayName("Should return LoginResponse on successful admin login")
    void testLogin_withValidAdminCredentials_returnsLoginResponse() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(org.springframework.security.core.Authentication.class));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(jwtUtil.generateToken("admin", "ADMIN")).thenReturn("mock.jwt.token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("admin", response.getUsername());
        assertEquals("ADMIN", response.getRole());
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("System Admin", response.getFullName());
    }

    @Test
    @DisplayName("Should throw BadRequestException on bad credentials")
    void testLogin_withInvalidCredentials_throwsBadRequestException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadRequestException.class, () -> authService.login(loginRequest));
    }

    @Test
    @DisplayName("Should throw BadRequestException when user account is disabled")
    void testLogin_withInactiveAccount_throwsBadRequestException() {
        adminUser.setActive(false);
        when(authenticationManager.authenticate(any())).thenReturn(mock(org.springframework.security.core.Authentication.class));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> authService.login(loginRequest));

        assertEquals("Account is disabled. Contact admin.", ex.getMessage());
    }

    @Test
    @DisplayName("Should include Bearer token type in response")
    void testLogin_includesBearerTokenType() {
        when(authenticationManager.authenticate(any())).thenReturn(mock(org.springframework.security.core.Authentication.class));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(jwtUtil.generateToken(any(), any())).thenReturn("token");

        LoginResponse response = authService.login(loginRequest);

        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    @DisplayName("Should return correct userId in response")
    void testLogin_returnsCorrectUserId() {
        when(authenticationManager.authenticate(any())).thenReturn(mock(org.springframework.security.core.Authentication.class));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(jwtUtil.generateToken(any(), any())).thenReturn("token");

        LoginResponse response = authService.login(loginRequest);

        assertEquals(1L, response.getUserId());
    }

    @Test
    @DisplayName("authenticationManager.authenticate should be called once")
    void testLogin_authManagerCalledOnce() {
        when(authenticationManager.authenticate(any())).thenReturn(mock(org.springframework.security.core.Authentication.class));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(jwtUtil.generateToken(any(), any())).thenReturn("token");

        authService.login(loginRequest);

        verify(authenticationManager, times(1)).authenticate(any());
    }
}
