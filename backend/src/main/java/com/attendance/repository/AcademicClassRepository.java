package com.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attendance.entity.AcademicClass;
import com.attendance.entity.Department;
import com.attendance.entity.Faculty;

public interface AcademicClassRepository extends JpaRepository<AcademicClass, Long> {
    List<AcademicClass> findByDepartment(Department department);
    List<AcademicClass> findByFaculty(Faculty faculty);
    Optional<AcademicClass> findByNameAndSectionAndSemester(String name, String section, Integer semester);
}
