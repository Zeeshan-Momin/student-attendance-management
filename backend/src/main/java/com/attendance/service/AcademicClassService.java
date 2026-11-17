package com.attendance.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.attendance.dto.ClassDto;
import com.attendance.dto.ClassRequest;
import com.attendance.entity.AcademicClass;
import com.attendance.entity.Department;
import com.attendance.entity.Faculty;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.repository.AcademicClassRepository;
import com.attendance.repository.DepartmentRepository;
import com.attendance.repository.FacultyRepository;

@Service
public class AcademicClassService {
    private final AcademicClassRepository academicClassRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;

    public AcademicClassService(AcademicClassRepository academicClassRepository,
                               DepartmentRepository departmentRepository,
                               FacultyRepository facultyRepository) {
        this.academicClassRepository = academicClassRepository;
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
    }

    public List<ClassDto> getAll() {
        return academicClassRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public ClassDto getById(Long id) {
        return toDto(academicClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + id)));
    }

    public ClassDto create(ClassRequest request) {
        Department department = departmentRepository.findByName(request.getDepartment())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartment()));
        Faculty faculty = null;
        if (request.getFacultyId() != null) {
            faculty = facultyRepository.findById(request.getFacultyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + request.getFacultyId()));
        }

        AcademicClass academicClass = new AcademicClass(request.getName(), request.getSection(), request.getSemester(), department);
        academicClass.setFaculty(faculty);
        return toDto(academicClassRepository.save(academicClass));
    }

    public ClassDto update(Long id, ClassRequest request) {
        AcademicClass academicClass = academicClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + id));
        Department department = departmentRepository.findByName(request.getDepartment())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartment()));
        Faculty faculty = null;
        if (request.getFacultyId() != null) {
            faculty = facultyRepository.findById(request.getFacultyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + request.getFacultyId()));
        }

        academicClass.setName(request.getName());
        academicClass.setSection(request.getSection());
        academicClass.setSemester(request.getSemester());
        academicClass.setDepartment(department);
        academicClass.setFaculty(faculty);
        return toDto(academicClassRepository.save(academicClass));
    }

    public void delete(Long id) {
        if (!academicClassRepository.existsById(id)) {
            throw new ResourceNotFoundException("Class not found with id: " + id);
        }
        academicClassRepository.deleteById(id);
    }

    private ClassDto toDto(AcademicClass academicClass) {
        return new ClassDto(
                academicClass.getId(),
                academicClass.getName(),
                academicClass.getSection(),
                academicClass.getSemester(),
                academicClass.getDepartment() != null ? academicClass.getDepartment().getName() : null,
                academicClass.getFaculty() != null ? academicClass.getFaculty().getFullName() : null
        );
    }
}
