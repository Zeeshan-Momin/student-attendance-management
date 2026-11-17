package com.attendance.dto;

public class ClassDto {
    private Long id;
    private String name;
    private String section;
    private Integer semester;
    private String department;
    private String facultyName;

    public ClassDto() {
    }

    public ClassDto(Long id, String name, String section, Integer semester, String department, String facultyName) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.semester = semester;
        this.department = department;
        this.facultyName = facultyName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
}
