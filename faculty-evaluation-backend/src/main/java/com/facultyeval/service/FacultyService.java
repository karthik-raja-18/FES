package com.facultyeval.service;

import com.facultyeval.dto.EvaluationResponse;
import com.facultyeval.dto.FacultyDashboardResponse;
import com.facultyeval.exception.ResourceNotFoundException;
import com.facultyeval.model.*;
import com.facultyeval.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultySubjectRepository facultySubjectRepository;
    private final EvaluationRepository evaluationRepository;

    public FacultyDashboardResponse getDashboard(String username) {
        Faculty faculty = facultyRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty profile not found for: " + username));

        // Overall stats
        Double overallAvg = evaluationRepository.findAverageRatingByFacultyId(faculty.getId());
        Long totalEvals = evaluationRepository.countByFacultyId(faculty.getId());

        // Rating distribution (1-5 stars)
        Map<Integer, Long> ratingDist = buildRatingDistribution(faculty.getId());

        // Per-subject summaries
        List<FacultySubject> assignments = facultySubjectRepository.findActiveByFacultyId(faculty.getId());
        List<FacultyDashboardResponse.SubjectRatingSummary> subjectSummaries = assignments.stream()
                .map(fs -> buildSubjectSummary(faculty, fs.getSubject()))
                .collect(Collectors.toList());

        // Recent anonymous feedback (all feedback comments)
        List<EvaluationResponse> recentFeedbacks = evaluationRepository
                .findFeedbackByFacultyId(faculty.getId()).stream()
                .sorted(Comparator.comparing(Evaluation::getSubmittedAt).reversed())
                .limit(20)
                .map(this::mapToEvaluationResponse)
                .collect(Collectors.toList());

        return FacultyDashboardResponse.builder()
                .facultyId(faculty.getId())
                .facultyName(faculty.getUser().getFullName())
                .department(faculty.getDepartment())
                .designation(faculty.getDesignation())
                .overallAverageRating(overallAvg != null ? roundTo2(overallAvg) : 0.0)
                .totalEvaluations(totalEvals)
                .ratingDistribution(ratingDist)
                .subjectSummaries(subjectSummaries)
                .recentFeedbacks(recentFeedbacks)
                .build();
    }

    private FacultyDashboardResponse.SubjectRatingSummary buildSubjectSummary(Faculty faculty, Subject subject) {
        Double avgRating = evaluationRepository.findAverageRatingByFacultyIdAndSubjectId(
                faculty.getId(), subject.getId());
        List<Evaluation> evals = evaluationRepository.findByFacultyAndSubject(faculty, subject);

        List<EvaluationResponse> feedbacks = evals.stream()
                .filter(e -> e.getFeedback() != null && !e.getFeedback().isBlank())
                .map(this::mapToEvaluationResponse)
                .collect(Collectors.toList());

        return FacultyDashboardResponse.SubjectRatingSummary.builder()
                .subjectId(subject.getId())
                .subjectName(subject.getName())
                .subjectCode(subject.getSubjectCode())
                .averageRating(avgRating != null ? roundTo2(avgRating) : 0.0)
                .evaluationCount((long) evals.size())
                .feedbacks(feedbacks)
                .build();
    }

    private Map<Integer, Long> buildRatingDistribution(Long facultyId) {
        // Initialize all stars to 0
        Map<Integer, Long> distribution = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) distribution.put(i, 0L);

        List<Object[]> results = evaluationRepository.findRatingDistributionByFacultyId(facultyId);
        for (Object[] row : results) {
            Integer star = (Integer) row[0];
            Long count = (Long) row[1];
            distribution.put(star, count);
        }
        return distribution;
    }

    // Anonymous - deliberately no student info
    private EvaluationResponse mapToEvaluationResponse(Evaluation eval) {
        return EvaluationResponse.builder()
                .id(eval.getId())
                .rating(eval.getRating())
                .feedback(eval.getFeedback())
                .subjectName(eval.getSubject().getName())
                .subjectCode(eval.getSubject().getSubjectCode())
                .submittedAt(eval.getSubmittedAt())
                .build();
    }

    private double roundTo2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
