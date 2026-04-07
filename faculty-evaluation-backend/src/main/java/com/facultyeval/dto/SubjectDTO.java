package com.facultyeval.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
    private Long id;

    @NotBlank(message = "Subject name is required")
    private String name;

    @NotBlank(message = "Subject code is required")
    private String subjectCode;

    private String description;
    private String semester;
    private String academicYear;
    private boolean active;
    private LocalDateTime createdAt;

    // Populated in responses
    private String facultyName;
    private Long facultyId;
    private int enrolledStudents;
}
