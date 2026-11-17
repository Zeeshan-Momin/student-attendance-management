package com.attendance.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.dto.ClassDto;
import com.attendance.dto.ClassRequest;
import com.attendance.service.AcademicClassService;

@RestController
@RequestMapping("/api")
public class ClassController {
    private final AcademicClassService academicClassService;

    public ClassController(AcademicClassService academicClassService) {
        this.academicClassService = academicClassService;
    }

    @GetMapping("/classes")
    public ResponseEntity<List<ClassDto>> getClasses() {
        return ResponseEntity.ok(academicClassService.getAll());
    }

    @PostMapping("/classes")
    public ResponseEntity<ClassDto> createClass(@Valid @RequestBody ClassRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academicClassService.create(request));
    }

    @PutMapping("/classes/{id}")
    public ResponseEntity<ClassDto> updateClass(@PathVariable Long id, @Valid @RequestBody ClassRequest request) {
        return ResponseEntity.ok(academicClassService.update(id, request));
    }

    @DeleteMapping("/classes/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        academicClassService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
