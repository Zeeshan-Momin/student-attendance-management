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

import com.attendance.dto.FacultyDto;
import com.attendance.dto.FacultyRequest;
import com.attendance.service.FacultyService;

@RestController
@RequestMapping("/api")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/faculty")
    public ResponseEntity<List<FacultyDto>> getFaculty() {
        return ResponseEntity.ok(facultyService.getAll());
    }

    @PostMapping("/faculty")
    public ResponseEntity<FacultyDto> createFaculty(@Valid @RequestBody FacultyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facultyService.create(request));
    }

    @PutMapping("/faculty/{id}")
    public ResponseEntity<FacultyDto> updateFaculty(@PathVariable Long id, @Valid @RequestBody FacultyRequest request) {
        return ResponseEntity.ok(facultyService.update(id, request));
    }

    @DeleteMapping("/faculty/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
