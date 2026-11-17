package com.attendance.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReportController {

    @GetMapping("/reports/daily")
    public ResponseEntity<Map<String, Object>> dailyReport() {
        return ResponseEntity.ok(Map.of("message", "Daily report placeholder"));
    }

    @GetMapping("/reports/monthly")
    public ResponseEntity<Map<String, Object>> monthlyReport() {
        return ResponseEntity.ok(Map.of("message", "Monthly report placeholder"));
    }

    @GetMapping("/reports/export")
    public ResponseEntity<Map<String, Object>> exportReport() {
        return ResponseEntity.ok(Map.of("message", "CSV export placeholder"));
    }
}
