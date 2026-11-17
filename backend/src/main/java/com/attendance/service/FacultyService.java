package com.attendance.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.attendance.dto.FacultyDto;
import com.attendance.dto.FacultyRequest;
import com.attendance.entity.Department;
import com.attendance.entity.Faculty;
import com.attendance.entity.User;
import com.attendance.entity.UserRole;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.repository.DepartmentRepository;
import com.attendance.repository.FacultyRepository;
import com.attendance.repository.UserRepository;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public FacultyService(FacultyRepository facultyRepository,
                         DepartmentRepository departmentRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<FacultyDto> getAll() {
        return facultyRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public FacultyDto getById(Long id) {
        return toDto(facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + id)));
    }

    public FacultyDto create(FacultyRequest request) {
        Department department = departmentRepository.findByName(request.getDepartment())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartment()));

        String username = request.getUsername() != null && !request.getUsername().isBlank()
                ? request.getUsername() : request.getEmail().split("@")[0];

        User user = userRepository.findByUsernameOrEmail(username, request.getEmail())
                .orElseGet(() -> userRepository.save(new User(username, request.getEmail(),
                        passwordEncoder.encode(request.getPassword() == null || request.getPassword().isBlank() ? "Password@123" : request.getPassword()), UserRole.FACULTY)));

        Faculty faculty = new Faculty(user, request.getFullName(), request.getEmail(), request.getPhoneNumber(), department, request.getDesignation());
        return toDto(facultyRepository.save(faculty));
    }

    public FacultyDto update(Long id, FacultyRequest request) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + id));
        Department department = departmentRepository.findByName(request.getDepartment())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartment()));

        faculty.setFullName(request.getFullName());
        faculty.setEmail(request.getEmail());
        faculty.setPhoneNumber(request.getPhoneNumber());
        faculty.setDepartment(department);
        faculty.setDesignation(request.getDesignation());
        return toDto(facultyRepository.save(faculty));
    }

    public void delete(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + id));
        facultyRepository.delete(faculty);
    }

    private FacultyDto toDto(Faculty faculty) {
        return new FacultyDto(
                faculty.getId(),
                faculty.getFullName(),
                faculty.getEmail(),
                faculty.getPhoneNumber(),
                faculty.getDepartment() != null ? faculty.getDepartment().getName() : null,
                faculty.getDesignation()
        );
    }
}
