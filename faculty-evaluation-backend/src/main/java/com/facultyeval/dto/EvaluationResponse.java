package com.facultyeval.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponse {
    private Long id;
    private Integer rating;
    private String feedback;
    private String subjectName;
    private String subjectCode;
    private LocalDateTime submittedAt;
    // NOTE: student identity is deliberately NOT included
}
