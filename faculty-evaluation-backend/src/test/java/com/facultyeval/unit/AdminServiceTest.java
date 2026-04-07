package com.facultyeval.unit;

import com.facultyeval.dto.FacultyDTO;
import com.facultyeval.dto.StudentDTO;
import com.facultyeval.dto.SubjectDTO;
import com.facultyeval.exception.BadRequestException;
import com.facultyeval.model.*;
import com.facultyeval.repository.*;
import com.facultyeval.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for AdminService
 * Tests subject, faculty, and student management operations.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AdminService Unit Tests")
class AdminServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private FacultyRepository facultyRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private FacultySubjectRepository facultySubjectRepository;
    @Mock private StudentEnrollmentRepository studentEnrollmentRepository;
    @Mock private EvaluationRepository evaluationRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    // ─── Subject Tests ────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Subject Management")
    class SubjectManagementTests {

        @Test
        @DisplayName("Should create subject when subject code doesn't exist")
        void testCreateSubject_withUniqueCode_createsSubject() {
            SubjectDTO dto = SubjectDTO.builder()
                    .name("Operating Systems").subjectCode("CS301")
                    .description("OS fundamentals").semester("5")
                    .academicYear("2024-25").build();

            Subject savedSubject = Subject.builder()
                    .id(1L).name("Operating Systems").subjectCode("CS301")
                    .description("OS fundamentals").semester("5")
                    .academicYear("2024-25").active(true).build();

            when(subjectRepository.existsBySubjectCode("CS301")).thenReturn(false);
            when(subjectRepository.save(any(Subject.class))).thenReturn(savedSubject);
            when(facultySubjectRepository.findFacultyBySubjectId(1L)).thenReturn(java.util.Collections.emptyList());
            when(studentEnrollmentRepository.findActiveBySubjectId(1L)).thenReturn(java.util.Collections.emptyList());

            SubjectDTO result = adminService.createSubject(dto);

            assertNotNull(result);
            assertEquals("CS301", result.getSubjectCode());
            assertEquals("Operating Systems", result.getName());
        }

        @Test
        @DisplayName("Should throw BadRequestException when subject code already exists")
        void testCreateSubject_withDuplicateCode_throwsException() {
            SubjectDTO dto = SubjectDTO.builder().subjectCode("CS301").build();
            when(subjectRepository.existsBySubjectCode("CS301")).thenReturn(true);

            BadRequestException ex = assertThrows(BadRequestException.class,
                    () -> adminService.createSubject(dto));
            assertTrue(ex.getMessage().contains("CS301"));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException for non-existent subject")
        void testGetSubjectById_withInvalidId_throwsException() {
            when(subjectRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(com.facultyeval.exception.ResourceNotFoundException.class,
                    () -> adminService.getSubjectById(999L));
        }
    }

    // ─── Faculty Tests ────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Faculty Management")
    class FacultyManagementTests {

        private FacultyDTO facultyDto;

        @BeforeEach
        void setUp() {
            facultyDto = FacultyDTO.builder()
                    .username("faculty01").password("Pass@123")
                    .fullName("Dr. John Doe").email("john@fes.edu")
                    .department("CS").designation("Professor")
                    .phoneNumber("9876543210").build();
        }

        @Test
        @DisplayName("Should create faculty when username and email are unique")
        void testCreateFaculty_withUniqueCredentials_createsFaculty() {
            User savedUser = User.builder()
                    .id(1L).username("faculty01").fullName("Dr. John Doe")
                    .email("john@fes.edu").role(Role.FACULTY).active(true).build();

            Faculty savedFaculty = Faculty.builder()
                    .id(1L).user(savedUser).department("CS")
                    .designation("Professor").phoneNumber("9876543210").build();

            when(userRepository.existsByUsername("faculty01")).thenReturn(false);
            when(userRepository.existsByEmail("john@fes.edu")).thenReturn(false);
            when(passwordEncoder.encode(any())).thenReturn("encodedPass");
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(facultyRepository.save(any(Faculty.class))).thenReturn(savedFaculty);
            when(evaluationRepository.findAverageRatingByFacultyId(anyLong())).thenReturn(null);
            when(evaluationRepository.countByFacultyId(anyLong())).thenReturn(0L);

            FacultyDTO result = adminService.createFaculty(facultyDto);

            assertNotNull(result);
            assertEquals("Dr. John Doe", result.getFullName());
            assertEquals("CS", result.getDepartment());
        }

        @Test
        @DisplayName("Should throw BadRequestException when username is taken")
        void testCreateFaculty_withDuplicateUsername_throwsException() {
            when(userRepository.existsByUsername("faculty01")).thenReturn(true);

            BadRequestException ex = assertThrows(BadRequestException.class,
                    () -> adminService.createFaculty(facultyDto));
            assertTrue(ex.getMessage().contains("faculty01"));
        }

        @Test
        @DisplayName("Should throw BadRequestException when email is already registered")
        void testCreateFaculty_withDuplicateEmail_throwsException() {
            when(userRepository.existsByUsername("faculty01")).thenReturn(false);
            when(userRepository.existsByEmail("john@fes.edu")).thenReturn(true);

            BadRequestException ex = assertThrows(BadRequestException.class,
                    () -> adminService.createFaculty(facultyDto));
            assertTrue(ex.getMessage().contains("john@fes.edu"));
        }
    }

    // ─── Student Tests ────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Student Management")
    class StudentManagementTests {

        @Test
        @DisplayName("Should create student when username and email are unique")
        void testCreateStudent_withUniqueCredentials_createsStudent() {
            StudentDTO dto = StudentDTO.builder()
                    .username("stu01").password("Pass@123")
                    .fullName("Alice Smith").email("alice@fes.edu")
                    .department("CS").program("B.Tech").batch("2022").build();

            User savedUser = User.builder()
                    .id(5L).username("stu01").fullName("Alice Smith")
                    .email("alice@fes.edu").role(Role.STUDENT).active(true).build();

            Student savedStudent = Student.builder()
                    .id(5L).user(savedUser).department("CS")
                    .program("B.Tech").batch("2022").build();

            when(userRepository.existsByUsername("stu01")).thenReturn(false);
            when(userRepository.existsByEmail("alice@fes.edu")).thenReturn(false);
            when(passwordEncoder.encode(any())).thenReturn("encodedPass");
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);
            when(studentEnrollmentRepository.findActiveByStudentId(anyLong())).thenReturn(java.util.Collections.emptyList());

            StudentDTO result = adminService.createStudent(dto);

            assertNotNull(result);
            assertEquals("Alice Smith", result.getFullName());
        }

        @Test
        @DisplayName("Should soft-delete student by setting user active=false")
        void testDeleteStudent_setsUserInactive() {
            User user = User.builder().id(5L).active(true).username("stu01")
                    .role(Role.STUDENT).fullName("A").email("a@b.com").build();
            Student student = Student.builder().id(5L).user(user)
                    .department("CS").program("B.Tech").batch("2022").build();

            when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
            when(studentRepository.save(any())).thenReturn(student);

            adminService.deleteStudent(5L);

            assertFalse(user.isActive());
            verify(studentRepository).save(student);
        }
    }
}
