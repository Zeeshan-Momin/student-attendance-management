package com.attendance.dto;

import java.time.LocalDate;

public class AttendanceEntryDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long classId;
    private LocalDate attendanceDate;
    private String status;

    public AttendanceEntryDto() {
    }

    public AttendanceEntryDto(Long id, Long studentId, String studentName, Long classId, LocalDate attendanceDate, String status) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.classId = classId;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
