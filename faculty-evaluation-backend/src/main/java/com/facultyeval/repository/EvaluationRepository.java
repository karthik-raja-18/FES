package com.facultyeval.repository;

import com.facultyeval.model.Evaluation;
import com.facultyeval.model.Faculty;
import com.facultyeval.model.Student;
import com.facultyeval.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByFaculty(Faculty faculty);

    List<Evaluation> findByStudent(Student student);

    List<Evaluation> findByFacultyAndSubject(Faculty faculty, Subject subject);

    Optional<Evaluation> findByStudentAndFacultyAndSubject(Student student, Faculty faculty, Subject subject);

    boolean existsByStudentAndFacultyAndSubject(Student student, Faculty faculty, Subject subject);

    // Average rating for a faculty across all subjects
    @Query("SELECT AVG(e.rating) FROM Evaluation e WHERE e.faculty.id = :facultyId")
    Double findAverageRatingByFacultyId(@Param("facultyId") Long facultyId);

    // Average rating for a faculty on a specific subject
    @Query("SELECT AVG(e.rating) FROM Evaluation e WHERE e.faculty.id = :facultyId AND e.subject.id = :subjectId")
    Double findAverageRatingByFacultyIdAndSubjectId(@Param("facultyId") Long facultyId,
                                                    @Param("subjectId") Long subjectId);

    // Count evaluations for a faculty
    @Query("SELECT COUNT(e) FROM Evaluation e WHERE e.faculty.id = :facultyId")
    Long countByFacultyId(@Param("facultyId") Long facultyId);

    // Rating distribution (count per star) for a faculty
    @Query("SELECT e.rating, COUNT(e) FROM Evaluation e WHERE e.faculty.id = :facultyId GROUP BY e.rating ORDER BY e.rating")
    List<Object[]> findRatingDistributionByFacultyId(@Param("facultyId") Long facultyId);

    // Check if student already submitted eval for this faculty in this subject
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Evaluation e " +
           "WHERE e.student.id = :studentId AND e.subject.id = :subjectId")
    boolean existsByStudentIdAndSubjectId(@Param("studentId") Long studentId,
                                          @Param("subjectId") Long subjectId);

    // Get all evaluations with feedback for a faculty (only non-empty feedback)
    @Query("SELECT e FROM Evaluation e WHERE e.faculty.id = :facultyId AND e.feedback IS NOT NULL AND e.feedback != ''")
    List<Evaluation> findFeedbackByFacultyId(@Param("facultyId") Long facultyId);

    // Admin: overall system statistics
    @Query("SELECT AVG(e.rating) FROM Evaluation e")
    Double findOverallAverageRating();

    @Query("SELECT COUNT(e) FROM Evaluation e")
    Long countTotalEvaluations();
}
