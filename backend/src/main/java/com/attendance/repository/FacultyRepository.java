package com.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attendance.entity.Department;
import com.attendance.entity.Faculty;
import com.attendance.entity.User;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByUser(User user);
    List<Faculty> findByDepartment(Department department);
}
