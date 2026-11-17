package com.attendance.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.attendance.entity.Department;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.repository.DepartmentRepository;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    public Department save(String name, String code) {
        Department department = new Department(name.trim(), code.trim().toUpperCase());
        return departmentRepository.save(department);
    }

    public Department update(Long id, String name, String code) {
        Department department = getById(id);
        department.setName(name.trim());
        department.setCode(code.trim().toUpperCase());
        return departmentRepository.save(department);
    }

    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    public String normalizeDepartmentName(String value) {
        return departmentRepository.findByName(value)
                .map(Department::getName)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + value));
    }
}
