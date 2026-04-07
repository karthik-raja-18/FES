package com.facultyeval.integration;

import com.facultyeval.model.Role;
import com.facultyeval.model.User;
import com.facultyeval.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests for AuthController
 * Tests the full login flow with H2 in-memory database.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("AuthController Integration Tests")
class AuthControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private FacultyRepository facultyRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private StudentEnrollmentRepository enrollmentRepository;
    @Autowired private FacultySubjectRepository facultySubjectRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Clean slate for each test - order is important for foreign keys
        facultySubjectRepository.deleteAll();
        enrollmentRepository.deleteAll();
        facultyRepository.deleteAll();
        studentRepository.deleteAll();
        userRepository.deleteAll();

        // Seed a test admin user
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("Admin@123"))
                .fullName("Test Admin")
                .email("admin@fes.test")
                .role(Role.ADMIN)
                .active(true)
                .build();
        userRepository.save(admin);
    }

    @Test
    @DisplayName("GET /api/auth/health should return 200 OK")
    void testHealthEndpoint_returns200() throws Exception {
        mockMvc.perform(get("/api/auth/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("OK"));
    }

    @Test
    @DisplayName("POST /api/auth/login with valid credentials returns JWT token")
    void testLogin_withValidCredentials_returnsToken() throws Exception {
        Map<String, String> payload = Map.of(
                "username", "admin",
                "password", "Admin@123"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.role").value("ADMIN"))
                .andExpect(jsonPath("$.data.username").value("admin"))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"));
    }

    @Test
    @DisplayName("POST /api/auth/login with wrong password returns 400")
    void testLogin_withWrongPassword_returns400() throws Exception {
        Map<String, String> payload = Map.of(
                "username", "admin",
                "password", "WrongPassword"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login with non-existent user returns 400")
    void testLogin_withUnknownUser_returns400() throws Exception {
        Map<String, String> payload = Map.of(
                "username", "ghost_user",
                "password", "any"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login with inactive account returns 400")
    void testLogin_withInactiveAccount_returns400() throws Exception {
        // Deactivate the admin user
        User admin = userRepository.findByUsername("admin").orElseThrow();
        admin.setActive(false);
        userRepository.save(admin);

        Map<String, String> payload = Map.of(
                "username", "admin",
                "password", "Admin@123"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login with missing username returns 400")
    void testLogin_withMissingUsername_returns400() throws Exception {
        Map<String, String> payload = Map.of("password", "Admin@123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }
}
