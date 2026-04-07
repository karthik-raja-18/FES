package com.facultyeval.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EvaluationRequest {

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Faculty ID is required")
    private Long facultyId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    @Size(max = 1000, message = "Feedback cannot exceed 1000 characters")
    private String feedback;
}
