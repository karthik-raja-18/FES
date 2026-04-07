package com.facultyeval.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollStudentRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;
}
