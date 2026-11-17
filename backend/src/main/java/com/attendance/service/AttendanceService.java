package com.attendance.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.dto.AttendanceEntryDto;
import com.attendance.dto.AttendanceRequest;
import com.attendance.entity.AcademicClass;
import com.attendance.entity.Attendance;
import com.attendance.entity.AttendanceStatus;
import com.attendance.entity.Student;
import com.attendance.exception.DuplicateAttendanceException;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.repository.AcademicClassRepository;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.StudentRepository;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final AcademicClassRepository academicClassRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                            StudentRepository studentRepository,
                            AcademicClassRepository academicClassRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.academicClassRepository = academicClassRepository;
    }

    @Transactional
    public List<AttendanceEntryDto> submitBulkAttendance(List<AttendanceRequest> requests) {
        List<AttendanceEntryDto> saved = new ArrayList<>();

        for (AttendanceRequest request : requests) {
            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));
            AcademicClass academicClass = academicClassRepository.findById(request.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + request.getClassId()));

            LocalDate date = request.getAttendanceDate();
            if (attendanceRepository.findByStudentAndAcademicClassAndAttendanceDate(student, academicClass, date).isPresent()) {
                throw new DuplicateAttendanceException("Attendance already exists for student " + student.getFullName() + " on " + date);
            }

            AttendanceStatus status = AttendanceStatus.valueOf(request.getStatus().toUpperCase());
            Attendance attendance = new Attendance(student, academicClass, date, status);
            Attendance savedAttendance = attendanceRepository.save(attendance);
            saved.add(new AttendanceEntryDto(savedAttendance.getId(), student.getId(), student.getFullName(), academicClass.getId(), date, status.name()));
        }

        return saved;
    }

    public List<AttendanceEntryDto> getAttendanceByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        return attendanceRepository.findByStudent(student).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AttendanceEntryDto> getAttendanceByClass(Long classId) {
        AcademicClass academicClass = academicClassRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
        return attendanceRepository.findByAcademicClass(academicClass).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AttendanceEntryDto> getAllAttendance() {
        return attendanceRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public AttendanceEntryDto updateAttendance(Long id, AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id: " + id));
        attendance.setStatus(AttendanceStatus.valueOf(request.getStatus().toUpperCase()));
        attendance.setAttendanceDate(request.getAttendanceDate());
        return mapToDto(attendanceRepository.save(attendance));
    }

    private AttendanceEntryDto mapToDto(Attendance attendance) {
        return new AttendanceEntryDto(
                attendance.getId(),
                attendance.getStudent().getId(),
                attendance.getStudent().getFullName(),
                attendance.getAcademicClass().getId(),
                attendance.getAttendanceDate(),
                attendance.getStatus().name());
    }
}
