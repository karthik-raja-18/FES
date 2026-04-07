package com.facultyeval.controller;

import com.facultyeval.dto.ApiResponse;
import com.facultyeval.dto.EvaluationRequest;
import com.facultyeval.dto.StudentSubjectResponse;
import com.facultyeval.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('STUDENT')")
public class StudentController {

    private final StudentService studentService;

    /**
     * GET /api/student/subjects
     * Returns all subjects the logged-in student is enrolled in,
     * along with faculty info and whether they've submitted an evaluation
     */
    @GetMapping("/subjects")
    public ResponseEntity<ApiResponse<List<StudentSubjectResponse>>> getMySubjects(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<StudentSubjectResponse> subjects =
                studentService.getEnrolledSubjects(userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.success("Enrolled subjects retrieved", subjects));
    }

    /**
     * POST /api/student/evaluate
     * Anonymously submit a star rating + written feedback for a faculty
     */
    @PostMapping("/evaluate")
    public ResponseEntity<ApiResponse<String>> submitEvaluation(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody EvaluationRequest request) {
        studentService.submitEvaluation(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Evaluation submitted successfully. Thank you for your feedback!"));
    }
}
