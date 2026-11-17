package com.attendance;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.attendance.dto.AttendanceRequest;
import com.attendance.entity.AcademicClass;
import com.attendance.entity.Attendance;
import com.attendance.entity.AttendanceStatus;
import com.attendance.entity.Department;
import com.attendance.entity.Student;
import com.attendance.exception.DuplicateAttendanceException;
import com.attendance.repository.AcademicClassRepository;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.service.AttendanceService;

class AttendanceServiceTest {

    @Test
    void submitBulkAttendanceShouldPreventDuplicateRecords() {
        AttendanceRepository attendanceRepository = mock(AttendanceRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        AcademicClassRepository academicClassRepository = mock(AcademicClassRepository.class);

        Department department = new Department("Computer Science", "CS");
        AcademicClass academicClass = new AcademicClass("Math", "A", 2, department);
        academicClass.setId(10L);
        Student student = new Student(null, "CS-101", "Alice Smith", "alice@example.com", "9876543210", department, 2, academicClass, LocalDate.now());
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(academicClassRepository.findById(10L)).thenReturn(Optional.of(academicClass));
        when(attendanceRepository.findByStudentAndAcademicClassAndAttendanceDate(student, academicClass, LocalDate.now()))
                .thenReturn(Optional.of(new Attendance(student, academicClass, LocalDate.now(), AttendanceStatus.PRESENT)));

        AttendanceService service = new AttendanceService(attendanceRepository, studentRepository, academicClassRepository);
        AttendanceRequest request = new AttendanceRequest();
        request.setStudentId(1L);
        request.setClassId(10L);
        request.setAttendanceDate(LocalDate.now());
        request.setStatus("PRESENT");

        DuplicateAttendanceException exception = assertThrows(DuplicateAttendanceException.class,
                () -> service.submitBulkAttendance(List.of(request)));
        assertEquals("Attendance already exists for student Alice Smith on " + LocalDate.now(), exception.getMessage());
    }

    @Test
    void submitBulkAttendanceShouldAllowValidRequest() {
        AttendanceRepository attendanceRepository = mock(AttendanceRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        AcademicClassRepository academicClassRepository = mock(AcademicClassRepository.class);

        Department department = new Department("Computer Science", "CS");
        AcademicClass academicClass = new AcademicClass("Math", "A", 2, department);
        academicClass.setId(10L);
        Student student = new Student(null, "CS-101", "Alice Smith", "alice@example.com", "9876543210", department, 2, academicClass, LocalDate.now());
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(academicClassRepository.findById(10L)).thenReturn(Optional.of(academicClass));
        when(attendanceRepository.findByStudentAndAcademicClassAndAttendanceDate(student, academicClass, LocalDate.now())).thenReturn(Optional.empty());
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> {
            Attendance a = invocation.getArgument(0);
            a.setId(99L);
            return a;
        });

        AttendanceService service = new AttendanceService(attendanceRepository, studentRepository, academicClassRepository);
        AttendanceRequest request = new AttendanceRequest();
        request.setStudentId(1L);
        request.setClassId(10L);
        request.setAttendanceDate(LocalDate.now());
        request.setStatus("LATE");

        assertDoesNotThrow(() -> service.submitBulkAttendance(List.of(request)));
    }
}
