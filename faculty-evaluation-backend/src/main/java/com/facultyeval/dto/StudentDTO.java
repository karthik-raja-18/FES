package com.facultyeval.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;

    // User fields
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Valid email is required")
    private String email;

    // Student-specific fields
    private String rollNumber;
    private String batch;
    private String program;
    private String department;
    private String phoneNumber;

    // Response fields
    private Long userId;
    private boolean active;
    private int enrolledSubjects;
}
