package com.facultyeval.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubjectResponse {
    private Long enrollmentId;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private String description;
    private String semester;
    private String academicYear;

    // Faculty assigned to teach this subject
    private Long facultyId;
    private String facultyName;
    private String facultyDepartment;
    private String facultyDesignation;

    // Has the student already evaluated this subject/faculty?
    private boolean alreadyEvaluated;
}
