package com.attendance.config;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.attendance.entity.AccountStatus;
import com.attendance.entity.AcademicClass;
import com.attendance.entity.Department;
import com.attendance.entity.Faculty;
import com.attendance.entity.Student;
import com.attendance.entity.User;
import com.attendance.entity.UserRole;
import com.attendance.repository.AcademicClassRepository;
import com.attendance.repository.DepartmentRepository;
import com.attendance.repository.FacultyRepository;
import com.attendance.repository.StudentRepository;
import com.attendance.repository.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final AcademicClassRepository academicClassRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository,
                          DepartmentRepository departmentRepository,
                          FacultyRepository facultyRepository,
                          StudentRepository studentRepository,
                          AcademicClassRepository academicClassRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.academicClassRepository = academicClassRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        Department cs = departmentRepository.save(new Department("Computer Science", "CS"));
        Department ee = departmentRepository.save(new Department("Electrical Engineering", "EE"));

        User adminUser = userRepository.save(new User("admin", "admin@college.edu", passwordEncoder.encode("Admin@123"), UserRole.ADMIN));
        User facultyUser = userRepository.save(new User("faculty1", "faculty1@college.edu", passwordEncoder.encode("Faculty@123"), UserRole.FACULTY));
        User studentUser = userRepository.save(new User("student1", "student1@college.edu", passwordEncoder.encode("Student@123"), UserRole.STUDENT));

        Faculty faculty = facultyRepository.save(new Faculty(facultyUser, "Dr. Rahul Sharma", "faculty1@college.edu", "9876543210", cs, "Professor"));
        AcademicClass classA = academicClassRepository.save(new AcademicClass("Data Structures", "A", 2, cs));
        classA.setFaculty(faculty);
        academicClassRepository.save(classA);

        Student student = studentRepository.save(new Student(studentUser, "CS-202", "Aisha Khan", "student1@college.edu", "9123456780", cs, 2, classA, LocalDate.now().minusMonths(2)));
        student.setAccountStatus(AccountStatus.ACTIVE);
        studentRepository.save(student);
    }
}
