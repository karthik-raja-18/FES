package com.facultyeval.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations",
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "faculty_id", "subject_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    @ToString.Exclude
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @ToString.Exclude
    private Subject subject;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer rating;

    @Column(length = 1000)
    private String feedback;

    // Always anonymous - student identity is hidden from faculty
    @Column(nullable = false)
    @Builder.Default
    private boolean anonymous = true;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime submittedAt = LocalDateTime.now();
}
