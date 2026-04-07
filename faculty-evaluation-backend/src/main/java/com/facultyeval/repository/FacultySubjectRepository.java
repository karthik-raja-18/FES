package com.facultyeval.repository;

import com.facultyeval.model.Faculty;
import com.facultyeval.model.FacultySubject;
import com.facultyeval.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultySubjectRepository extends JpaRepository<FacultySubject, Long> {

    List<FacultySubject> findByFaculty(Faculty faculty);

    List<FacultySubject> findBySubject(Subject subject);

    Optional<FacultySubject> findByFacultyAndSubject(Faculty faculty, Subject subject);

    boolean existsByFacultyAndSubject(Faculty faculty, Subject subject);

    @Query("SELECT fs FROM FacultySubject fs WHERE fs.faculty.id = :facultyId AND fs.active = true")
    List<FacultySubject> findActiveByFacultyId(Long facultyId);

    @Query("SELECT fs FROM FacultySubject fs WHERE fs.subject.id = :subjectId AND fs.active = true")
    List<FacultySubject> findActiveBySubjectId(Long subjectId);

    // Get faculty teaching a specific subject (for student view)
    @Query("SELECT fs.faculty FROM FacultySubject fs WHERE fs.subject.id = :subjectId AND fs.active = true")
    List<Faculty> findFacultyBySubjectId(Long subjectId);
}
