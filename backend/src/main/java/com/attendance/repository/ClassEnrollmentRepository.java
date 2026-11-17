package com.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attendance.entity.AcademicClass;
import com.attendance.entity.ClassEnrollment;
import com.attendance.entity.Student;

public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment, Long> {
    List<ClassEnrollment> findByAcademicClass(AcademicClass academicClass);
    List<ClassEnrollment> findByStudent(Student student);
    Optional<ClassEnrollment> findByStudentAndAcademicClass(Student student, AcademicClass academicClass);
}
