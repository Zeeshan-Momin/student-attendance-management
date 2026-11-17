package com.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.attendance.entity.AcademicClass;
import com.attendance.entity.Department;
import com.attendance.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRollNumber(String rollNumber);

    List<Student> findByDepartmentAndSemester(Department department, Integer semester);

    List<Student> findByAcademicClass(AcademicClass academicClass);

    @Query("select s from Student s where lower(s.fullName) like lower(concat('%', :query, '%')) or lower(s.rollNumber) like lower(concat('%', :query, '%'))")
    List<Student> searchStudents(String query);
}
