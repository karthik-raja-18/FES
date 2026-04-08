package com.facultyeval.service;

import com.facultyeval.dto.EvaluationRequest;
import com.facultyeval.dto.StudentSubjectResponse;
import com.facultyeval.exception.BadRequestException;
import com.facultyeval.exception.ResourceNotFoundException;
import com.facultyeval.model.*;
import com.facultyeval.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;
    private final StudentEnrollmentRepository enrollmentRepository;
    private final FacultySubjectRepository facultySubjectRepository;
    private final EvaluationRepository evaluationRepository;

    // Get all subjects the student is enrolled in
    @Transactional(readOnly = true)
    public List<StudentSubjectResponse> getEnrolledSubjects(String username) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for: " + username));

        List<StudentEnrollment> enrollments = enrollmentRepository.findActiveByStudentId(student.getId());

        return enrollments.stream()
                .map(enrollment -> mapToSubjectResponse(enrollment, student))
                .collect(Collectors.toList());
    }

    // Submit evaluation (anonymous)
    @Transactional
    public void submitEvaluation(String username, EvaluationRequest request) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + username));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + request.getSubjectId()));

        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found: " + request.getFacultyId()));

        // Verify student is enrolled in this subject
        boolean enrolled = enrollmentRepository.existsByStudentAndSubject(student, subject);
        if (!enrolled) {
            throw new BadRequestException("You are not enrolled in this subject");
        }

        // Verify faculty teaches this subject
        boolean teaches = facultySubjectRepository.existsByFacultyAndSubject(faculty, subject);
        if (!teaches) {
            throw new BadRequestException("This faculty does not teach this subject");
        }

        // Prevent duplicate evaluation
        if (evaluationRepository.existsByStudentIdAndSubjectId(student.getId(), subject.getId())) {
            throw new BadRequestException("You have already submitted an evaluation for this subject");
        }

        Evaluation evaluation = Evaluation.builder()
                .student(student)
                .faculty(faculty)
                .subject(subject)
                .rating(request.getRating())
                .feedback(request.getFeedback())
                .anonymous(true)
                .build();

        evaluationRepository.save(evaluation);
        log.info("Evaluation submitted: Student {} -> Faculty {} for Subject {} | Rating: {}",
                student.getId(), faculty.getId(), subject.getId(), request.getRating());
    }

    private StudentSubjectResponse mapToSubjectResponse(StudentEnrollment enrollment, Student student) {
        Subject subject = enrollment.getSubject();
        StudentSubjectResponse response = StudentSubjectResponse.builder()
                .enrollmentId(enrollment.getId())
                .subjectId(subject.getId())
                .subjectName(subject.getName())
                .subjectCode(subject.getSubjectCode())
                .description(subject.getDescription())
                .semester(subject.getSemester())
                .academicYear(subject.getAcademicYear())
                .build();

        // Attach faculty info
        List<Faculty> faculties = facultySubjectRepository.findFacultyBySubjectId(subject.getId());
        if (faculties != null && !faculties.isEmpty()) {
            Faculty faculty = faculties.get(0);
            if (faculty != null) {
                response.setFacultyId(faculty.getId());
                if (faculty.getUser() != null) {
                    response.setFacultyName(faculty.getUser().getFullName());
                }
                response.setFacultyDepartment(faculty.getDepartment());
                response.setFacultyDesignation(faculty.getDesignation());
            }
        }

        // Check if already evaluated
        boolean evaluated = evaluationRepository.existsByStudentIdAndSubjectId(
                student.getId(), subject.getId());
        response.setAlreadyEvaluated(evaluated);

        return response;
    }
}
