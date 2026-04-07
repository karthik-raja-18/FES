package com.facultyeval.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "faculties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String department;

    private String designation;

    private String employeeId;

    private String phoneNumber;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FacultySubject> assignedSubjects;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Evaluation> evaluations;
}
