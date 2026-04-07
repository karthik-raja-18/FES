package com.facultyeval.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignFacultyRequest {

    @NotNull(message = "Faculty ID is required")
    private Long facultyId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;
}
