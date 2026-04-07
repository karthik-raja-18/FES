package com.facultyeval.repository;

import com.facultyeval.model.Student;
import com.facultyeval.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByRollNumber(String rollNumber);

    @Query("SELECT s FROM Student s JOIN s.user u WHERE u.username = :username")
    Optional<Student> findByUsername(String username);
}
