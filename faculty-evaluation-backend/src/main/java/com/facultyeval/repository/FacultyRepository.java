package com.facultyeval.repository;

import com.facultyeval.model.Faculty;
import com.facultyeval.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByUser(User user);
    Optional<Faculty> findByUserId(Long userId);
    Optional<Faculty> findByEmployeeId(String employeeId);

    @Query("SELECT f FROM Faculty f JOIN f.user u WHERE u.username = :username")
    Optional<Faculty> findByUsername(String username);

    List<Faculty> findByDepartment(String department);
}
