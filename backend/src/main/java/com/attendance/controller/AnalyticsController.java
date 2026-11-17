package com.attendance.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.service.AnalyticsService;

@RestController
@RequestMapping("/api")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/analytics/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(analyticsService.dashboard());
    }

    @GetMapping("/analytics/monthly")
    public ResponseEntity<Map<String, Object>> monthly() {
        return ResponseEntity.ok(Map.of("message", "Monthly analytics placeholder"));
    }

    @GetMapping("/analytics/class/{classId}")
    public ResponseEntity<Map<String, Object>> classAnalytics(@PathVariable Long classId) {
        return ResponseEntity.ok(Map.of("classId", classId, "message", "Class analytics placeholder"));
    }

    @GetMapping("/analytics/student/{studentId}")
    public ResponseEntity<Map<String, Object>> studentAnalytics(@PathVariable Long studentId) {
        return ResponseEntity.ok(Map.of("studentId", studentId, "message", "Student analytics placeholder"));
    }
}
