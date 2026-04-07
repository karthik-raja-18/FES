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

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests for StudentController
 * Tests enrolled subjects retrieval and evaluation submission.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("StudentController Integration Tests")
class StudentControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private FacultyRepository facultyRepository;
    @Autowired private StudentEnrollmentRepository enrollmentRepository;
    @Autowired private FacultySubjectRepository facultySubjectRepository;
    @Autowired private EvaluationRepository evaluationRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private ObjectMapper objectMapper;

    private String studentToken;
    private Subject subject;
    private Faculty faculty;
    private Student student;

    @BeforeEach
    void setUp() {
        // Teardown
        evaluationRepository.deleteAll();
        enrollmentRepository.deleteAll();
        facultySubjectRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
        subjectRepository.deleteAll();
        userRepository.deleteAll();

        // Create student user
        User studentUser = User.builder()
                .username("student01").password(passwordEncoder.encode("Pass@123"))
                .fullName("Alice Smith").email("alice@fes.test")
                .role(Role.STUDENT).active(true).build();
        studentUser = userRepository.save(studentUser);

        student = Student.builder()
                .user(studentUser).department("CS")
                .program("B.Tech").batch("2022").build();
        student = studentRepository.save(student);

        // Create faculty user
        User facultyUser = User.builder()
                .username("faculty01").password(passwordEncoder.encode("Pass@123"))
                .fullName("Dr. Bob").email("bob@fes.test")
                .role(Role.FACULTY).active(true).build();
        facultyUser = userRepository.save(facultyUser);

        faculty = Faculty.builder()
                .user(facultyUser).department("CS")
                .designation("Professor").build();
        faculty = facultyRepository.save(faculty);

        // Create subject
        subject = Subject.builder()
                .name("Data Structures").subjectCode("CS201")
                .description("DS").semester("3").academicYear("2024-25")
                .active(true).build();
        subject = subjectRepository.save(subject);

        // Enroll student in subject
        StudentEnrollment enrollment = StudentEnrollment.builder()
                .student(student).subject(subject).active(true).build();
        enrollmentRepository.save(enrollment);

        // Assign faculty to subject
        FacultySubject fs = FacultySubject.builder()
                .faculty(faculty).subject(subject).active(true).build();
        facultySubjectRepository.save(fs);

        studentToken = jwtUtil.generateToken("student01", "STUDENT");
    }

    @Test
    @DisplayName("GET /api/student/subjects — Returns enrolled subjects for student")
    void testGetEnrolledSubjects_returnsSubjectList() throws Exception {
        mockMvc.perform(get("/api/student/subjects")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].subjectName").value("Data Structures"))
                .andExpect(jsonPath("$.data[0].alreadyEvaluated").value(false));
    }

    @Test
    @DisplayName("POST /api/student/evaluate — Student can submit evaluation")
    void testSubmitEvaluation_withValidData_succeeds() throws Exception {
        Map<String, Object> payload = Map.of(
                "subjectId", subject.getId(),
                "facultyId", faculty.getId(),
                "rating", 4,
                "feedback", "Good teaching!"
        );

        mockMvc.perform(post("/api/student/evaluate")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("POST /api/student/evaluate — Duplicate evaluation returns 400")
    void testSubmitEvaluation_duplicate_returns400() throws Exception {
        // Submit once
        Evaluation eval = Evaluation.builder()
                .student(student).faculty(faculty).subject(subject)
                .rating(5).feedback("Great!").anonymous(true).build();
        evaluationRepository.save(eval);

        Map<String, Object> payload = Map.of(
                "subjectId", subject.getId(),
                "facultyId", faculty.getId(),
                "rating", 3,
                "feedback", "Trying again"
        );

        mockMvc.perform(post("/api/student/evaluate")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/student/subjects — Without token returns 403/401")
    void testGetEnrolledSubjects_withoutToken_returnsForbidden() throws Exception {
        mockMvc.perform(get("/api/student/subjects"))
                .andExpect(status().isForbidden());
    }
}
