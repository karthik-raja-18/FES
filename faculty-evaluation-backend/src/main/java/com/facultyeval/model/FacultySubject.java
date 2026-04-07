package com.facultyeval.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "faculty_subjects",
       uniqueConstraints = @UniqueConstraint(columnNames = {"faculty_id", "subject_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacultySubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime assignedAt = LocalDateTime.now();
}
