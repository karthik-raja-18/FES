package com.facultyeval.controller;

import com.facultyeval.dto.ApiResponse;
import com.facultyeval.dto.LoginRequest;
import com.facultyeval.dto.LoginResponse;
import com.facultyeval.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * Used by Admin, Faculty, and Student to authenticate
     * Returns JWT token + role for frontend routing
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Login successful", authService.login(request)));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<LoginResponse>> updateProfile(
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
            @RequestBody com.facultyeval.dto.ProfileUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully",
                authService.updateProfile(userDetails.getUsername(), request)));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Faculty Evaluation API is running", "OK"));
    }
}
