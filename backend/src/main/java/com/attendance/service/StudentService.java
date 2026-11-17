package com.attendance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.dto.StudentDto;
import com.attendance.dto.StudentRequest;
import com.attendance.entity.AccountStatus;
import com.attendance.entity.AcademicClass;
import com.attendance.entity.ClassEnrollment;
import com.attendance.entity.Department;
import com.attendance.entity.Student;
import com.attendance.entity.User;
import com.attendance.entity.UserRole;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.repository.AcademicClassRepository;
import com.attendance.repository.ClassEnrollmentRepository;
import com.attendance.repository.DepartmentRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.repository.UserRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final AcademicClassRepository academicClassRepository;
    private final ClassEnrollmentRepository classEnrollmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                         DepartmentRepository departmentRepository,
                         AcademicClassRepository academicClassRepository,
                         ClassEnrollmentRepository classEnrollmentRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.academicClassRepository = academicClassRepository;
        this.classEnrollmentRepository = classEnrollmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public StudentDto getStudent(Long id) {
        return toDto(studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id)));
    }

    public StudentDto createStudent(StudentRequest request, String password) {
        Department department = departmentRepository.findByName(request.getDepartment())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartment()));

        Student existing = studentRepository.findByRollNumber(request.getRollNumber()).orElse(null);
        if (existing != null) {
            throw new IllegalArgumentException("Roll number already exists");
        }

        String username = request.getFullName().replaceAll("\\s+", "").toLowerCase() + "_" + request.getSemester();
        User user = userRepository.findByUsernameOrEmail(username, request.getEmail())
                .orElseGet(() -> userRepository.save(new User(username, request.getEmail(), passwordEncoder.encode(Optional.ofNullable(password).orElse("Password@123")), UserRole.STUDENT)));

        AcademicClass academicClass = null;
        if (request.getClassName() != null && !request.getClassName().isBlank()) {
            academicClass = academicClassRepository.findByNameAndSectionAndSemester(request.getClassName(), "A", request.getSemester())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + request.getClassName()));
        }

        Student student = new Student(user, request.getRollNumber(), request.getFullName(), request.getEmail(), request.getPhoneNumber(),
                department, request.getSemester(), academicClass, request.getEnrollmentDate());
        student.setAccountStatus(AccountStatus.valueOf(request.getAccountStatus().toUpperCase()));
        Student saved = studentRepository.save(student);

        if (academicClass != null) {
            classEnrollmentRepository.save(new ClassEnrollment(saved, academicClass));
        }

        return toDto(saved);
    }

    public StudentDto updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        Department department = departmentRepository.findByName(request.getDepartment())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartment()));

        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setDepartment(department);
        student.setSemester(request.getSemester());
        student.setEnrollmentDate(request.getEnrollmentDate());
        student.setAccountStatus(AccountStatus.valueOf(request.getAccountStatus().toUpperCase()));

        if (request.getClassName() != null && !request.getClassName().isBlank()) {
            AcademicClass academicClass = academicClassRepository.findByNameAndSectionAndSemester(request.getClassName(), "A", request.getSemester())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + request.getClassName()));
            student.setAcademicClass(academicClass);
            if (classEnrollmentRepository.findByStudentAndAcademicClass(student, academicClass).isEmpty()) {
                classEnrollmentRepository.save(new ClassEnrollment(student, academicClass));
            }
        } else {
            student.setAcademicClass(null);
        }

        return toDto(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
    }

    public List<StudentDto> searchStudents(String query) {
        return studentRepository.searchStudents(query).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<StudentDto> filterStudents(String departmentName, Integer semester) {
        if (departmentName != null && !departmentName.isBlank() && semester != null) {
            Department department = departmentRepository.findByName(departmentName)
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + departmentName));
            return studentRepository.findByDepartmentAndSemester(department, semester).stream().map(this::toDto).collect(Collectors.toList());
        }
        return getAllStudents();
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsForClass(Long classId) {
        AcademicClass academicClass = academicClassRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
        return studentRepository.findByAcademicClass(academicClass);
    }

    private StudentDto toDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getRollNumber(),
                student.getFullName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getDepartment() != null ? student.getDepartment().getName() : null,
                student.getSemester(),
                student.getAcademicClass() != null ? student.getAcademicClass().getName() : null,
                student.getEnrollmentDate(),
                student.getAccountStatus() != null ? student.getAccountStatus().name() : null
        );
    }
}
