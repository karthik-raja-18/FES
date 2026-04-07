package com.facultyeval.service;

import com.facultyeval.dto.*;
import com.facultyeval.exception.BadRequestException;
import com.facultyeval.exception.ResourceNotFoundException;
import com.facultyeval.model.*;
import com.facultyeval.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final FacultySubjectRepository facultySubjectRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final EvaluationRepository evaluationRepository;
    private final PasswordEncoder passwordEncoder;

    // ============================
    // SUBJECT MANAGEMENT
    // ============================

    @Transactional
    public SubjectDTO createSubject(SubjectDTO dto) {
        if (subjectRepository.existsBySubjectCode(dto.getSubjectCode())) {
            throw new BadRequestException("Subject code already exists: " + dto.getSubjectCode());
        }
        Subject subject = Subject.builder()
                .name(dto.getName())
                .subjectCode(dto.getSubjectCode())
                .description(dto.getDescription())
                .semester(dto.getSemester())
                .academicYear(dto.getAcademicYear())
                .active(true)
                .build();
        subject = subjectRepository.save(subject);
        log.info("Created subject: {} ({})", subject.getName(), subject.getSubjectCode());
        return mapToSubjectDTO(subject);
    }

    public List<SubjectDTO> getAllSubjects() {
        try {
            return subjectRepository.findAll().stream()
                    .map(this::mapToSubjectDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all subjects: {}", e.getMessage(), e);
            throw e;
        }
    }

    public SubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
        return mapToSubjectDTO(subject);
    }

    @Transactional
    public SubjectDTO updateSubject(Long id, SubjectDTO dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + id));
        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());
        subject.setSemester(dto.getSemester());
        subject.setAcademicYear(dto.getAcademicYear());
        subject = subjectRepository.save(subject);
        return mapToSubjectDTO(subject);
    }

    @Transactional
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + id));
        subject.setActive(false);
        subjectRepository.save(subject);
    }

    // ============================
    // FACULTY MANAGEMENT
    // ============================

    @Transactional
    public FacultyDTO createFaculty(FacultyDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username already taken: " + dto.getUsername());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already registered: " + dto.getEmail());
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .role(Role.FACULTY)
                .active(true)
                .build();
        user = userRepository.save(user);

        Faculty faculty = Faculty.builder()
                .user(user)
                .department(dto.getDepartment())
                .designation(dto.getDesignation())
                .phoneNumber(dto.getPhoneNumber())
                .build();
        faculty = facultyRepository.save(faculty);

        // Auto-generate employee ID based on the DB-generated ID
        faculty.setEmployeeId("FAC-" + String.format("%04d", faculty.getId()));
        faculty = facultyRepository.save(faculty);

        log.info("Created faculty: {} ({})", user.getFullName(), faculty.getDepartment());
        return mapToFacultyDTO(faculty);
    }

    public List<FacultyDTO> getAllFaculties() {
        return facultyRepository.findAll().stream()
                .map(this::mapToFacultyDTO)
                .collect(Collectors.toList());
    }

    public FacultyDTO getFacultyById(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found: " + id));
        return mapToFacultyDTO(faculty);
    }

    @Transactional
    public FacultyDTO updateFaculty(Long id, FacultyDTO dto) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found: " + id));
        faculty.setDepartment(dto.getDepartment());
        faculty.setDesignation(dto.getDesignation());
        faculty.setPhoneNumber(dto.getPhoneNumber());
        faculty.getUser().setFullName(dto.getFullName());
        faculty.getUser().setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            faculty.getUser().setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        faculty = facultyRepository.save(faculty);
        return mapToFacultyDTO(faculty);
    }

    @Transactional
    public void deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found: " + id));
        faculty.getUser().setActive(false);
        facultyRepository.save(faculty);
    }

    // ============================
    // STUDENT MANAGEMENT
    // ============================

    @Transactional
    public StudentDTO createStudent(StudentDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username already taken: " + dto.getUsername());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already registered: " + dto.getEmail());
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .role(Role.STUDENT)
                .active(true)
                .build();
        user = userRepository.save(user);

        Student student = Student.builder()
                .user(user)
                .batch(dto.getBatch())
                .program(dto.getProgram())
                .department(dto.getDepartment())
                .phoneNumber(dto.getPhoneNumber())
                .build();
        student = studentRepository.save(student);

        // Auto-generate roll number based on the DB-generated ID
        student.setRollNumber("STU-" + String.format("%04d", student.getId()));
        student = studentRepository.save(student);

        log.info("Created student: {} ({})", user.getFullName(), student.getRollNumber());
        return mapToStudentDTO(student);
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToStudentDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        return mapToStudentDTO(student);
    }

    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        student.setBatch(dto.getBatch());
        student.setProgram(dto.getProgram());
        student.setDepartment(dto.getDepartment());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.getUser().setFullName(dto.getFullName());
        student.getUser().setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            student.getUser().setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        student = studentRepository.save(student);
        return mapToStudentDTO(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        student.getUser().setActive(false);
        studentRepository.save(student);
    }

    // ============================
    // FACULTY-SUBJECT ASSIGNMENT
    // ============================

    @Transactional
    public ApiResponse<String> assignFacultyToSubject(AssignFacultyRequest request) {
        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found: " + request.getFacultyId()));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + request.getSubjectId()));

        if (facultySubjectRepository.existsByFacultyAndSubject(faculty, subject)) {
            throw new BadRequestException("Faculty is already assigned to this subject");
        }

        FacultySubject fs = FacultySubject.builder()
                .faculty(faculty)
                .subject(subject)
                .active(true)
                .build();
        facultySubjectRepository.save(fs);
        log.info("Assigned faculty {} to subject {}", faculty.getUser().getFullName(), subject.getName());
        return ApiResponse.success("Faculty assigned to subject successfully");
    }

    @Transactional
    public ApiResponse<String> unassignFacultyFromSubject(AssignFacultyRequest request) {
        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        FacultySubject fs = facultySubjectRepository.findByFacultyAndSubject(faculty, subject)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        fs.setActive(false);
        facultySubjectRepository.save(fs);
        return ApiResponse.success("Faculty unassigned from subject successfully");
    }

    // ============================
    // STUDENT ENROLLMENT
    // ============================

    @Transactional
    public ApiResponse<String> enrollStudentInSubject(EnrollStudentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + request.getSubjectId()));

        if (studentEnrollmentRepository.existsByStudentAndSubject(student, subject)) {
            throw new BadRequestException("Student is already enrolled in this subject");
        }

        StudentEnrollment enrollment = StudentEnrollment.builder()
                .student(student)
                .subject(subject)
                .active(true)
                .build();
        studentEnrollmentRepository.save(enrollment);
        log.info("Enrolled student {} in subject {}", student.getUser().getFullName(), subject.getName());
        return ApiResponse.success("Student enrolled successfully");
    }

    @Transactional
    public ApiResponse<String> unenrollStudentFromSubject(EnrollStudentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        StudentEnrollment enrollment = studentEnrollmentRepository.findByStudentAndSubject(student, subject)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        enrollment.setActive(false);
        studentEnrollmentRepository.save(enrollment);
        return ApiResponse.success("Student unenrolled from subject successfully");
    }

    // ============================
    // DASHBOARD STATISTICS
    // ============================

    public AdminDashboardResponse getDashboardStats() {
        long totalSubjects = subjectRepository.count();
        long totalFaculties = facultyRepository.count();
        long totalStudents = studentRepository.count();
        long totalEvaluations = evaluationRepository.countTotalEvaluations();
        Double overallAvgRating = evaluationRepository.findOverallAverageRating();
        long totalEnrollments = studentEnrollmentRepository.count();
        long pendingEvaluations = totalEnrollments - totalEvaluations;

        // Fetch top 5 faculties by rating
        List<FacultyDTO> topFaculties = facultyRepository.findAll().stream()
                .map(this::mapToFacultyDTO)
                .filter(f -> f.getAverageRating() != null && f.getAverageRating() > 0)
                .sorted((f1, f2) -> f2.getAverageRating().compareTo(f1.getAverageRating()))
                .limit(5)
                .collect(Collectors.toList());

        return AdminDashboardResponse.builder()
                .totalSubjects(totalSubjects)
                .totalFaculties(totalFaculties)
                .totalStudents(totalStudents)
                .totalEvaluations(totalEvaluations)
                .overallAverageRating(overallAvgRating != null ? Math.round(overallAvgRating * 100.0) / 100.0 : 0.0)
                .pendingEvaluations(Math.max(0, pendingEvaluations))
                .topFaculties(topFaculties)
                .build();
    }

    // ============================
    // MAPPERS
    // ============================

    private SubjectDTO mapToSubjectDTO(Subject subject) {
        SubjectDTO dto = SubjectDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .subjectCode(subject.getSubjectCode())
                .description(subject.getDescription())
                .semester(subject.getSemester())
                .academicYear(subject.getAcademicYear())
                .active(subject.isActive())
                .createdAt(subject.getCreatedAt())
                .build();

        // Attach faculty info if assigned
        try {
            List<Faculty> faculties = facultySubjectRepository.findFacultyBySubjectId(subject.getId());
            if (faculties != null && !faculties.isEmpty()) {
                Faculty faculty = faculties.get(0);
                if (faculty != null) {
                    dto.setFacultyId(faculty.getId());
                    if (faculty.getUser() != null) {
                        dto.setFacultyName(faculty.getUser().getFullName());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error fetching faculty for subjectId {}: {}", subject.getId(), e.getMessage());
        }

        // Count enrolled students
        try {
            List<StudentEnrollment> enrollments = studentEnrollmentRepository.findActiveBySubjectId(subject.getId());
            dto.setEnrolledStudents(enrollments != null ? enrollments.size() : 0);
        } catch (Exception e) {
            log.warn("Error fetching enrollments for subjectId {}: {}", subject.getId(), e.getMessage());
            dto.setEnrolledStudents(0);
        }

        return dto;
    }

    public FacultyDTO mapToFacultyDTO(Faculty faculty) {
        Double avgRating = evaluationRepository.findAverageRatingByFacultyId(faculty.getId());
        Long totalEvals = evaluationRepository.countByFacultyId(faculty.getId());
        return FacultyDTO.builder()
                .id(faculty.getId())
                .userId(faculty.getUser().getId())
                .username(faculty.getUser().getUsername())
                .fullName(faculty.getUser().getFullName())
                .email(faculty.getUser().getEmail())
                .department(faculty.getDepartment())
                .designation(faculty.getDesignation())
                .employeeId(faculty.getEmployeeId())
                .phoneNumber(faculty.getPhoneNumber())
                .active(faculty.getUser().isActive())
                .averageRating(avgRating != null ? Math.round(avgRating * 100.0) / 100.0 : null)
                .totalEvaluations(totalEvals)
                .build();
    }

    public StudentDTO mapToStudentDTO(Student student) {
        long enrolledCount = studentEnrollmentRepository.findActiveByStudentId(student.getId()).size();
        return StudentDTO.builder()
                .id(student.getId())
                .userId(student.getUser().getId())
                .username(student.getUser().getUsername())
                .fullName(student.getUser().getFullName())
                .email(student.getUser().getEmail())
                .rollNumber(student.getRollNumber())
                .batch(student.getBatch())
                .program(student.getProgram())
                .department(student.getDepartment())
                .phoneNumber(student.getPhoneNumber())
                .active(student.getUser().isActive())
                .enrolledSubjects((int) enrolledCount)
                .build();
    }
}
