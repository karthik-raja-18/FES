package com.facultyeval.controller;

import com.facultyeval.dto.*;
import com.facultyeval.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    // ============================
    // DASHBOARD
    // ============================

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {
        return ResponseEntity.ok(
                ApiResponse.success("Dashboard stats retrieved", adminService.getDashboardStats()));
    }

    // ============================
    // SUBJECT ENDPOINTS
    // ============================

    @PostMapping("/subjects")
    public ResponseEntity<ApiResponse<SubjectDTO>> createSubject(
            @Valid @RequestBody SubjectDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subject created successfully", adminService.createSubject(dto)));
    }

    @GetMapping("/subjects")
    public ResponseEntity<ApiResponse<List<SubjectDTO>>> getAllSubjects() {
        return ResponseEntity.ok(
                ApiResponse.success("Subjects retrieved", adminService.getAllSubjects()));
    }

    @GetMapping("/subjects/{id}")
    public ResponseEntity<ApiResponse<SubjectDTO>> getSubject(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Subject retrieved", adminService.getSubjectById(id)));
    }

    @PutMapping("/subjects/{id}")
    public ResponseEntity<ApiResponse<SubjectDTO>> updateSubject(
            @PathVariable Long id, @Valid @RequestBody SubjectDTO dto) {
        return ResponseEntity.ok(
                ApiResponse.success("Subject updated successfully", adminService.updateSubject(id, dto)));
    }

    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSubject(@PathVariable Long id) {
        adminService.deleteSubject(id);
        return ResponseEntity.ok(ApiResponse.success("Subject deactivated successfully"));
    }

    // ============================
    // FACULTY ENDPOINTS
    // ============================

    @PostMapping("/faculties")
    public ResponseEntity<ApiResponse<FacultyDTO>> createFaculty(
            @Valid @RequestBody FacultyDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Faculty created successfully", adminService.createFaculty(dto)));
    }

    @GetMapping("/faculties")
    public ResponseEntity<ApiResponse<List<FacultyDTO>>> getAllFaculties() {
        return ResponseEntity.ok(
                ApiResponse.success("Faculties retrieved", adminService.getAllFaculties()));
    }

    @GetMapping("/faculties/{id}")
    public ResponseEntity<ApiResponse<FacultyDTO>> getFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Faculty retrieved", adminService.getFacultyById(id)));
    }

    @PutMapping("/faculties/{id}")
    public ResponseEntity<ApiResponse<FacultyDTO>> updateFaculty(
            @PathVariable Long id, @RequestBody FacultyDTO dto) {
        return ResponseEntity.ok(
                ApiResponse.success("Faculty updated successfully", adminService.updateFaculty(id, dto)));
    }

    @DeleteMapping("/faculties/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFaculty(@PathVariable Long id) {
        adminService.deleteFaculty(id);
        return ResponseEntity.ok(ApiResponse.success("Faculty deactivated successfully"));
    }

    // ============================
    // STUDENT ENDPOINTS
    // ============================

    @PostMapping("/students")
    public ResponseEntity<ApiResponse<StudentDTO>> createStudent(
            @Valid @RequestBody StudentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student created successfully", adminService.createStudent(dto)));
    }

    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getAllStudents() {
        return ResponseEntity.ok(
                ApiResponse.success("Students retrieved", adminService.getAllStudents()));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Student retrieved", adminService.getStudentById(id)));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> updateStudent(
            @PathVariable Long id, @RequestBody StudentDTO dto) {
        return ResponseEntity.ok(
                ApiResponse.success("Student updated successfully", adminService.updateStudent(id, dto)));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable Long id) {
        adminService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deactivated successfully"));
    }

    // ============================
    // FACULTY-SUBJECT ASSIGNMENT
    // ============================

    @PostMapping("/assign-faculty")
    public ResponseEntity<ApiResponse<String>> assignFaculty(
            @Valid @RequestBody AssignFacultyRequest request) {
        return ResponseEntity.ok(adminService.assignFacultyToSubject(request));
    }

    @PostMapping("/unassign-faculty")
    public ResponseEntity<ApiResponse<String>> unassignFaculty(
            @Valid @RequestBody AssignFacultyRequest request) {
        return ResponseEntity.ok(adminService.unassignFacultyFromSubject(request));
    }

    // ============================
    // STUDENT ENROLLMENT
    // ============================

    @PostMapping("/enroll-student")
    public ResponseEntity<ApiResponse<String>> enrollStudent(
            @Valid @RequestBody EnrollStudentRequest request) {
        return ResponseEntity.ok(adminService.enrollStudentInSubject(request));
    }

    @PostMapping("/unenroll-student")
    public ResponseEntity<ApiResponse<String>> unenrollStudent(
            @Valid @RequestBody EnrollStudentRequest request) {
        return ResponseEntity.ok(adminService.unenrollStudentFromSubject(request));
    }
}
