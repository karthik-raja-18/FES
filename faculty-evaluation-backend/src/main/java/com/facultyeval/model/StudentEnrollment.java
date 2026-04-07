package com.facultyeval.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_enrollments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime enrolledAt = LocalDateTime.now();
}
