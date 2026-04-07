package com.facultyeval.integration;

import com.facultyeval.config.JwtUtil;
import com.facultyeval.model.*;
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

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests for AdminController
 * Tests full CRUD operations for subjects, faculty, and students.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("AdminController Integration Tests")
class AdminControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private FacultyRepository facultyRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private ObjectMapper objectMapper;

    private String adminToken;

    @BeforeEach
    void setUp() {
        // Clear data
        subjectRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
        userRepository.deleteAll();

        // Create admin user
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("Admin@123"))
                .fullName("Test Admin")
                .email("admin@fes.test")
                .role(Role.ADMIN)
                .active(true)
                .build();
        userRepository.save(admin);

        adminToken = jwtUtil.generateToken("admin", "ADMIN");
    }

    // ─── Subject CRUD ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/admin/subjects — Admin can create a subject")
    void testCreateSubject_asAdmin_returns200() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Data Structures");
        payload.put("subjectCode", "CS201");
        payload.put("description", "DS & Algorithms");
        payload.put("semester", "3");
        payload.put("academicYear", "2024-25");

        mockMvc.perform(post("/api/admin/subjects")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.subjectCode").value("CS201"))
                .andExpect(jsonPath("$.data.name").value("Data Structures"));
    }

    @Test
    @DisplayName("POST /api/admin/subjects — Duplicate subject code returns 400")
    void testCreateSubject_withDuplicateCode_returns400() throws Exception {
        Subject existing = Subject.builder()
                .name("Networks").subjectCode("CS401")
                .description("Networking").semester("6")
                .academicYear("2024-25").active(true).build();
        subjectRepository.save(existing);

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Networks II");
        payload.put("subjectCode", "CS401");
        payload.put("description", "Advanced Networking");
        payload.put("semester", "7");
        payload.put("academicYear", "2024-25");

        mockMvc.perform(post("/api/admin/subjects")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/admin/subjects — Returns list of all subjects")
    void testGetAllSubjects_returnsSubjectList() throws Exception {
        Subject s = Subject.builder()
                .name("Math").subjectCode("MA101")
                .description("Calculus").semester("1")
                .academicYear("2024-25").active(true).build();
        subjectRepository.save(s);

        mockMvc.perform(get("/api/admin/subjects")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].subjectCode").value("MA101"));
    }

    @Test
    @DisplayName("GET /api/admin/dashboard — Admin dashboard stats accessible")
    void testGetDashboard_returnsDashboardStats() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalSubjects").isNumber())
                .andExpect(jsonPath("$.data.totalFaculties").isNumber())
                .andExpect(jsonPath("$.data.totalStudents").isNumber());
    }

    @Test
    @DisplayName("GET /api/admin/subjects — Without token returns 403/401")
    void testGetAllSubjects_withoutToken_returnsForbidden() throws Exception {
        mockMvc.perform(get("/api/admin/subjects"))
                .andExpect(status().isForbidden());
    }

    // ─── Faculty CRUD ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/admin/faculty — Admin can create a faculty member")
    void testCreateFaculty_asAdmin_returns200() throws Exception {
        Map<String, Object> payload = Map.of(
                "username", "faculty01",
                "password", "Pass@123",
                "fullName", "Dr. Jane Doe",
                "email", "jane@fes.test",
                "department", "CS",
                "designation", "Professor",
                "phoneNumber", "9876543210"
        );

        mockMvc.perform(post("/api/admin/faculties")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.username").value("faculty01"))
                .andExpect(jsonPath("$.data.department").value("CS"));
    }

    // ─── Student CRUD ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/admin/students — Admin can create a student")
    void testCreateStudent_asAdmin_returns200() throws Exception {
        Map<String, Object> payload = Map.of(
                "username", "student01",
                "password", "Pass@123",
                "fullName", "Alice Wonderland",
                "email", "alice@fes.test",
                "department", "CS",
                "program", "B.Tech",
                "batch", "2022",
                "phoneNumber", "9123456789"
        );

        mockMvc.perform(post("/api/admin/students")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.username").value("student01"))
                .andExpect(jsonPath("$.data.program").value("B.Tech"));
    }
}
