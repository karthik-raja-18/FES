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
public class FacultyDTO {
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

    // Faculty-specific fields
    @NotBlank(message = "Department is required")
    private String department;

    private String designation;
    private String employeeId;
    private String phoneNumber;

    // Response fields
    private Long userId;
    private boolean active;
    private Double averageRating;
    private Long totalEvaluations;
}
