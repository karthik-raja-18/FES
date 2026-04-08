package com.facultyeval.config;

import com.facultyeval.model.Role;
import com.facultyeval.model.User;
import com.facultyeval.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.facultyeval.model.*;
import com.facultyeval.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentEnrollmentRepository enrollmentRepository;
    private final FacultySubjectRepository facultySubjectRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .email("admin@faculty-eval.com")
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            log.info("Admin user created successfully! Username: 'admin', Password: 'admin123'");
        }

        if (userRepository.findByUsername("faculty01").isEmpty()) {
            User faculty = User.builder()
                    .username("faculty01")
                    .password(passwordEncoder.encode("faculty123"))
                    .fullName("Dr. Jane Doe")
                    .email("faculty@faculty-eval.com")
                    .role(Role.FACULTY)
                    .active(true)
                    .build();
            userRepository.save(faculty);
        }

        User facultyUser = userRepository.findByUsername("faculty01").orElse(null);
        if (facultyUser != null && facultyRepository.findByUser(facultyUser).isEmpty()) {
            facultyRepository.save(Faculty.builder()
                    .user(facultyUser)
                    .department("Computer Science")
                    .designation("Associate Professor")
                    .employeeId("EMP001")
                    .build());
        }

        if (userRepository.findByUsername("student01").isEmpty()) {
            User student = User.builder()
                    .username("student01")
                    .password(passwordEncoder.encode("student123"))
                    .fullName("Alice Smith")
                    .email("student@faculty-eval.com")
                    .role(Role.STUDENT)
                    .active(true)
                    .build();
            userRepository.save(student);
        }

        User studentUser = userRepository.findByUsername("student01").orElse(null);
        if (studentUser != null && studentRepository.findByUser(studentUser).isEmpty()) {
            studentRepository.save(Student.builder()
                    .user(studentUser)
                    .rollNumber("STU001")
                    .batch("2022")
                    .program("B.Tech")
                    .department("Computer Science")
                    .build());
        }

        // Seed a sample subject and enrollment if not exists for E2E tests
        if (subjectRepository.findBySubjectCode("CS101").isEmpty()) {
            Subject subject = subjectRepository.save(Subject.builder()
                    .subjectCode("CS101")
                    .name("Introduction to Programming")
                    .semester("SPRING")
                    .academicYear("2024")
                    .build());

            Faculty faculty = facultyRepository.findByUsername("faculty01").orElse(null);
            Student student = studentRepository.findByUsername("student01").orElse(null);

            if (faculty != null) {
                facultySubjectRepository.save(FacultySubject.builder()
                        .faculty(faculty)
                        .subject(subject)
                        .build());
            }

            if (student != null) {
                enrollmentRepository.save(StudentEnrollment.builder()
                        .student(student)
                        .subject(subject)
                        .active(true)
                        .build());
            }
        }
        // Seed E2E Specific Credentials for Testing
        if (userRepository.findByUsername("student1").isEmpty()) {
            User e2eStudent = userRepository.save(User.builder()
                    .username("student1")
                    .password(passwordEncoder.encode("student1"))
                    .fullName("Test Student")
                    .email("student1@e2e.test")
                    .role(Role.STUDENT)
                    .active(true)
                    .build());
            
            studentRepository.save(Student.builder()
                    .user(e2eStudent)
                    .rollNumber("E2E001")
                    .batch("2024")
                    .program("B.Tech")
                    .department("Computer Science")
                    .build());
            log.info("E2E Student user 'student1' created");
        }

        if (userRepository.findByUsername("faculty1").isEmpty()) {
            User e2eFaculty = userRepository.save(User.builder()
                    .username("faculty1")
                    .password(passwordEncoder.encode("faculty1"))
                    .fullName("Test Faculty")
                    .email("faculty1@e2e.test")
                    .role(Role.FACULTY)
                    .active(true)
                    .build());
            
            facultyRepository.save(Faculty.builder()
                    .user(e2eFaculty)
                    .department("Computer Science")
                    .designation("E2E Test Professor")
                    .employeeId("E2EEMP01")
                    .build());
            log.info("E2E Faculty user 'faculty1' created");
        }
    }
}
