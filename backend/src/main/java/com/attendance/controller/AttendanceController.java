package com.attendance.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.dto.AttendanceEntryDto;
import com.attendance.dto.AttendanceRequest;
import com.attendance.service.AttendanceService;

@RestController
@RequestMapping("/api")
public class AttendanceController {
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/attendance/bulk")
    public ResponseEntity<List<AttendanceEntryDto>> bulkAttendance(@Valid @RequestBody List<AttendanceRequest> requests) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.submitBulkAttendance(requests));
    }

    @GetMapping("/attendance")
    public ResponseEntity<List<AttendanceEntryDto>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    @GetMapping("/attendance/student/{studentId}")
    public ResponseEntity<List<AttendanceEntryDto>> getAttendanceByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
    }

    @GetMapping("/attendance/class/{classId}")
    public ResponseEntity<List<AttendanceEntryDto>> getAttendanceByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByClass(classId));
    }

    @PutMapping("/attendance/{id}")
    public ResponseEntity<AttendanceEntryDto> updateAttendance(@PathVariable Long id, @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.updateAttendance(id, request));
    }
}
