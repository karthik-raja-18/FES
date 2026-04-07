package com.facultyeval.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String department;
    private String password; // optional new password
}
