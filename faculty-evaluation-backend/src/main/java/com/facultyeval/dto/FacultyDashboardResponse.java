package com.facultyeval.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDashboardResponse {
    private Long facultyId;
    private String facultyName;
    private String department;
    private String designation;
    private Double overallAverageRating;
    private Long totalEvaluations;
    private Map<Integer, Long> ratingDistribution; // star -> count
    private List<SubjectRatingSummary> subjectSummaries;
    private List<EvaluationResponse> recentFeedbacks;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectRatingSummary {
        private Long subjectId;
        private String subjectName;
        private String subjectCode;
        private Double averageRating;
        private Long evaluationCount;
        private List<EvaluationResponse> feedbacks;
    }
}
