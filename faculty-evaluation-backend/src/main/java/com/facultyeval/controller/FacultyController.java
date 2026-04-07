package com.facultyeval.controller;

import com.facultyeval.dto.ApiResponse;
import com.facultyeval.dto.FacultyDashboardResponse;
import com.facultyeval.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculty")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('FACULTY')")
public class FacultyController {

    private final FacultyService facultyService;

    /**
     * GET /api/faculty/dashboard
     * Returns the faculty's overall average rating, per-subject breakdown,
     * rating distribution, and all anonymous student feedback
     */
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<FacultyDashboardResponse>> getMyDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {
        FacultyDashboardResponse dashboard =
                facultyService.getDashboard(userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.success("Dashboard loaded successfully", dashboard));
    }
}
