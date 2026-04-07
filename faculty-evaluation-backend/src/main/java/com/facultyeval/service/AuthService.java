package com.facultyeval.service;

import com.facultyeval.config.JwtUtil;
import com.facultyeval.dto.LoginRequest;
import com.facultyeval.dto.LoginResponse;
import com.facultyeval.exception.BadRequestException;
import com.facultyeval.model.Faculty;
import com.facultyeval.model.Role;
import com.facultyeval.model.Student;
import com.facultyeval.model.User;
import com.facultyeval.repository.FacultyRepository;
import com.facultyeval.repository.StudentRepository;
import com.facultyeval.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Invalid username or password");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.isActive()) {
            throw new BadRequestException("Account is disabled. Contact admin.");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        // Resolve profile info (faculty/student specific)
        Long profileId = null;
        String department = null;
        String phoneNumber = null;
        
        if (user.getRole() == Role.FACULTY) {
            Optional<Faculty> f = facultyRepository.findByUser(user);
            profileId = f.map(Faculty::getId).orElse(null);
            department = f.map(Faculty::getDepartment).orElse(null);
            phoneNumber = f.map(Faculty::getPhoneNumber).orElse(null);
        } else if (user.getRole() == Role.STUDENT) {
            Optional<Student> s = studentRepository.findByUser(user);
            profileId = s.map(Student::getId).orElse(null);
            department = s.map(Student::getDepartment).orElse(null);
            phoneNumber = s.map(Student::getPhoneNumber).orElse(null);
        }

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .role(user.getRole().name())
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profileId(profileId)
                .department(department)
                .email(user.getEmail())
                .phoneNumber(phoneNumber)
                .build();
    }

    private Long resolveProfileId(User user) {
        if (user.getRole() == Role.FACULTY) {
            return facultyRepository.findByUser(user)
                    .map(Faculty::getId)
                    .orElse(null);
        } else if (user.getRole() == Role.STUDENT) {
            return studentRepository.findByUser(user)
                    .map(Student::getId)
                    .orElse(null);
        }
        return null; // Admin has no profile ID
    }

    @org.springframework.transaction.annotation.Transactional
    public LoginResponse updateProfile(String username, com.facultyeval.dto.ProfileUpdateRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user = userRepository.save(user);

        Long profileId = null;
        String department = null;

        if (user.getRole() == Role.FACULTY) {
            Faculty f = facultyRepository.findByUser(user)
                    .orElseThrow(() -> new BadRequestException("Faculty record not found"));
            if (request.getPhoneNumber() != null) f.setPhoneNumber(request.getPhoneNumber());
            facultyRepository.save(f);
            profileId = f.getId();
            department = f.getDepartment();
        } else if (user.getRole() == Role.STUDENT) {
            Student s = studentRepository.findByUser(user)
                    .orElseThrow(() -> new BadRequestException("Student record not found"));
            if (request.getPhoneNumber() != null) s.setPhoneNumber(request.getPhoneNumber());
            studentRepository.save(s);
            profileId = s.getId();
            department = s.getDepartment();
        }

        return LoginResponse.builder()
                .token(null) // Token doesn't change
                .tokenType("Bearer")
                .role(user.getRole().name())
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profileId(profileId)
                .department(department)
                .build();
    }
}
