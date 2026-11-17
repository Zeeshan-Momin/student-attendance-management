package com.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.attendance.entity.AcademicClass;
import com.attendance.entity.Attendance;
import com.attendance.entity.AttendanceStatus;
import com.attendance.entity.Student;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent(Student student);
    List<Attendance> findByAcademicClass(AcademicClass academicClass);
    List<Attendance> findByAcademicClassAndAttendanceDate(AcademicClass academicClass, LocalDate attendanceDate);
    Optional<Attendance> findByStudentAndAcademicClassAndAttendanceDate(Student student, AcademicClass academicClass, LocalDate attendanceDate);

    @Query("select a from Attendance a where a.attendanceDate between :start and :end order by a.attendanceDate")
    List<Attendance> findByDateRange(LocalDate start, LocalDate end);

    @Query("select count(a) from Attendance a where a.student = :student and a.status = :status")
    long countByStudentAndStatus(Student student, AttendanceStatus status);

    @Query("select count(a) from Attendance a where a.student = :student")
    long countByStudent(Student student);
}
