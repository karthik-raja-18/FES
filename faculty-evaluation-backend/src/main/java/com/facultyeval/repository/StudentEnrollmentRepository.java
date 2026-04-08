package com.facultyeval.repository;

import com.facultyeval.model.Student;
import com.facultyeval.model.StudentEnrollment;
import com.facultyeval.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {

    List<StudentEnrollment> findByStudent(Student student);

    List<StudentEnrollment> findBySubject(Subject subject);

    Optional<StudentEnrollment> findByStudentAndSubject(Student student, Subject subject);

    boolean existsByStudentAndSubject(Student student, Subject subject);

    @Query("SELECT se FROM StudentEnrollment se WHERE se.student.id = :studentId AND se.active = true")
    List<StudentEnrollment> findActiveByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT se FROM StudentEnrollment se WHERE se.subject.id = :subjectId AND se.active = true")
    List<StudentEnrollment> findActiveBySubjectId(@Param("subjectId") Long subjectId);
}
