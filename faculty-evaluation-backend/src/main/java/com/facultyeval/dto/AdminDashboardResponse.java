package com.facultyeval.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalSubjects;
    private long totalFaculties;
    private long totalStudents;
    private long totalEvaluations;
    private Double overallAverageRating;
    private long pendingEvaluations; // enrolled students who haven't evaluated yet
    private java.util.List<FacultyDTO> topFaculties;
}
