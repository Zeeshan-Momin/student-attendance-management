# Student Attendance Management System

A full-stack Student Attendance Management System built with Spring Boot 3, Java 17, MySQL, React, Vite, and Tailwind CSS.

## Overview

This project provides role-based management for admin, faculty, and student roles with dashboard analytics, attendance tracking, and report generation.

## Tech Stack

- Backend: Java 17, Spring Boot 3.3.x, Spring Security, JWT, Spring Data JPA, Hibernate, Flyway, MySQL
- Frontend: React, Vite, JavaScript, Tailwind CSS, React Router, Axios, Recharts, Lucide React, React Toastify
- Testing: JUnit 5, Mockito

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.9+
- Node.js 18+
- MySQL 8+

### Environment

Create a `.env` file from `.env.example` and update your database and JWT values.

### Backend

```bash
cd backend
mvn clean verify
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Default Accounts

- Admin: username `admin`, password `Admin@123`
- Faculty: username `faculty1`, password `Faculty@123`
- Student: username `student1`, password `Student@123`

## Routes

- API base: http://localhost:8080/api
- Frontend: http://localhost:5173

## Jenkins

A sample Jenkinsfile is included at the project root for CI workflow support.

## Testing

```bash
cd backend && mvn clean verify
cd frontend && npm ci && npm run build
```
