package com.facultyeval.unit;

import com.facultyeval.dto.EvaluationRequest;
import com.facultyeval.dto.StudentSubjectResponse;
import com.facultyeval.exception.BadRequestException;
import com.facultyeval.exception.ResourceNotFoundException;
import com.facultyeval.model.*;
import com.facultyeval.repository.*;
import com.facultyeval.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for StudentService
 * Tests enrolled subjects retrieval and evaluation submission logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Unit Tests")
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private FacultyRepository facultyRepository;
    @Mock private StudentEnrollmentRepository enrollmentRepository;
    @Mock private FacultySubjectRepository facultySubjectRepository;
    @Mock private EvaluationRepository evaluationRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private Subject subject;
    private Faculty faculty;
    private User studentUser;
    private User facultyUser;

    @BeforeEach
    void setUp() {
        studentUser = User.builder()
                .id(10L).username("student01")
                .fullName("Alice Smith").email("alice@fes.edu")
                .role(Role.STUDENT).active(true).build();

        facultyUser = User.builder()
                .id(20L).username("faculty01")
                .fullName("Dr. Bob").email("bob@fes.edu")
                .role(Role.FACULTY).active(true).build();

        student = Student.builder()
                .id(1L).user(studentUser)
                .department("CS").program("B.Tech").batch("2022").build();

        faculty = Faculty.builder()
                .id(1L).user(facultyUser)
                .department("CS").designation("Professor").build();

        subject = Subject.builder()
                .id(1L).name("Data Structures").subjectCode("CS201")
                .description("DS & Algorithms").semester("3").academicYear("2024-25")
                .active(true).build();
    }

    // ─── getEnrolledSubjects ───────────────────────────────────────────────────

    @Test
    @DisplayName("Should return list of enrolled subjects for valid student")
    void testGetEnrolledSubjects_withValidStudent_returnsList() {
        StudentEnrollment enrollment = StudentEnrollment.builder()
                .id(1L).student(student).subject(subject).active(true).build();

        when(studentRepository.findByUsername("student01")).thenReturn(Optional.of(student));
        when(enrollmentRepository.findActiveByStudentId(1L)).thenReturn(List.of(enrollment));
        when(facultySubjectRepository.findFacultyBySubjectId(1L)).thenReturn(List.of(faculty));
        when(evaluationRepository.existsByStudentIdAndSubjectId(1L, 1L)).thenReturn(false);

        List<StudentSubjectResponse> result = studentService.getEnrolledSubjects("student01");

        assertEquals(1, result.size());
        assertEquals("Data Structures", result.get(0).getSubjectName());
        assertFalse(result.get(0).isAlreadyEvaluated());
    }

    @Test
    @DisplayName("Should return empty list when student has no enrollments")
    void testGetEnrolledSubjects_withNoEnrollments_returnsEmptyList() {
        when(studentRepository.findByUsername("student01")).thenReturn(Optional.of(student));
        when(enrollmentRepository.findActiveByStudentId(1L)).thenReturn(Collections.emptyList());

        List<StudentSubjectResponse> result = studentService.getEnrolledSubjects("student01");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for unknown student username")
    void testGetEnrolledSubjects_withUnknownStudent_throwsException() {
        when(studentRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> studentService.getEnrolledSubjects("ghost"));
    }

    // ─── submitEvaluation ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Should save evaluation when all conditions are met")
    void testSubmitEvaluation_withValidData_savesEvaluation() {
        EvaluationRequest request = new EvaluationRequest();
        request.setSubjectId(1L);
        request.setFacultyId(1L);
        request.setRating(5);
        request.setFeedback("Excellent!");

        when(studentRepository.findByUsername("student01")).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(enrollmentRepository.existsByStudentAndSubject(student, subject)).thenReturn(true);
        when(facultySubjectRepository.existsByFacultyAndSubject(faculty, subject)).thenReturn(true);
        when(evaluationRepository.existsByStudentIdAndSubjectId(1L, 1L)).thenReturn(false);

        assertDoesNotThrow(() -> studentService.submitEvaluation("student01", request));
        verify(evaluationRepository, times(1)).save(any(Evaluation.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when student not enrolled in subject")
    void testSubmitEvaluation_whenNotEnrolled_throwsBadRequest() {
        EvaluationRequest request = new EvaluationRequest();
        request.setSubjectId(1L);
        request.setFacultyId(1L);
        request.setRating(4);

        when(studentRepository.findByUsername("student01")).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(enrollmentRepository.existsByStudentAndSubject(student, subject)).thenReturn(false);

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> studentService.submitEvaluation("student01", request));
        assertEquals("You are not enrolled in this subject", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException on duplicate evaluation submission")
    void testSubmitEvaluation_whenDuplicateEvaluation_throwsBadRequest() {
        EvaluationRequest request = new EvaluationRequest();
        request.setSubjectId(1L);
        request.setFacultyId(1L);
        request.setRating(3);

        when(studentRepository.findByUsername("student01")).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(enrollmentRepository.existsByStudentAndSubject(student, subject)).thenReturn(true);
        when(facultySubjectRepository.existsByFacultyAndSubject(faculty, subject)).thenReturn(true);
        when(evaluationRepository.existsByStudentIdAndSubjectId(1L, 1L)).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> studentService.submitEvaluation("student01", request));
        assertTrue(ex.getMessage().contains("already submitted"));
    }

    @Test
    @DisplayName("Should throw BadRequestException when faculty does not teach subject")
    void testSubmitEvaluation_whenFacultyDoesNotTeachSubject_throwsBadRequest() {
        EvaluationRequest request = new EvaluationRequest();
        request.setSubjectId(1L);
        request.setFacultyId(1L);
        request.setRating(4);

        when(studentRepository.findByUsername("student01")).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(enrollmentRepository.existsByStudentAndSubject(student, subject)).thenReturn(true);
        when(facultySubjectRepository.existsByFacultyAndSubject(faculty, subject)).thenReturn(false);

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> studentService.submitEvaluation("student01", request));
        assertEquals("This faculty does not teach this subject", ex.getMessage());
    }
}
