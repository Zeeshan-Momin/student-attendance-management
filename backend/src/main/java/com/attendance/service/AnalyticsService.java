package com.attendance.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.attendance.entity.Attendance;
import com.attendance.entity.AttendanceStatus;
import com.attendance.entity.Student;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.StudentRepository;

@Service
public class AnalyticsService {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AnalyticsService(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    public Map<String, Object> dashboard() {
        long totalStudents = studentRepository.count();
        long totalFaculty = 0L;
        long totalClasses = 0L;
        long todayPresent = 0L;
        long todayAbsent = 0L;
        double overallPercent = 0.0;

        List<Attendance> todayEntries = attendanceRepository.findByDateRange(LocalDate.now(), LocalDate.now());
        for (Attendance entry : todayEntries) {
            if (entry.getStatus() == AttendanceStatus.PRESENT) {
                todayPresent++;
            } else if (entry.getStatus() == AttendanceStatus.ABSENT) {
                todayAbsent++;
            }
        }

        List<Attendance> all = attendanceRepository.findAll();
        if (!all.isEmpty()) {
            long presentCount = all.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
            long totalRecorded = all.size();
            overallPercent = totalRecorded == 0 ? 0 : (presentCount * 100.0) / totalRecorded;
        }

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalStudents", totalStudents);
        dashboard.put("totalFaculty", totalFaculty);
        dashboard.put("totalClasses", totalClasses);
        dashboard.put("todayPresentStudents", todayPresent);
        dashboard.put("todayAbsentStudents", todayAbsent);
        dashboard.put("overallAttendancePercentage", overallPercent);
        dashboard.put("recentAttendance", todayEntries);
        return dashboard;
    }
}
