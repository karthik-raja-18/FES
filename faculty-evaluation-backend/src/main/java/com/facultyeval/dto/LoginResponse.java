package com.facultyeval.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    @Builder.Default
    private String tokenType = "Bearer";
    private String role;
    private Long userId;
    private String username;
    private String fullName;
    private Long profileId; // Faculty ID or Student ID depending on role
    private String department;
    private String email;
    private String phoneNumber;
}
