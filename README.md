# 📚 Pustakaalay

**Pustakaalay** is a full-stack Smart Library Management System designed to manage books, physical book copies, users, borrowing records, overdue books, and fines through a secure role-based application.

The project uses a **Spring Boot REST API**, **React frontend**, **MySQL database**, and **JWT-based authentication**.

---

## Features

### Authentication & Security
- JWT-based authentication
- BCrypt password hashing
- Role-based authorization
- ADMIN and MEMBER roles
- Protected REST APIs
- Protected frontend routes

### Book Management
- View book catalogue
- Search books
- Manage book information
- Manage physical book copies
- Track copy availability and condition

### Borrowing Management
- Issue books
- Return books
- Track due dates
- Track borrowing status
- Detect overdue borrowings

### Fine Management
- Automatic overdue fine calculation
- Track pending and paid fines
- Associate fines with borrowing records

### User Management
- Manage library users
- Membership numbers
- User account status
- Role-based access

---

## Technology Stack

### Backend
- Java 21
- Spring Boot 4.1
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- Maven

### Frontend
- React 19
- React Router
- Vite
- JavaScript
- CSS

### Database
- MySQL 8

### Development
- Git
- GitHub
- Google Cloud Shell
- Docker

---

## Project Structure

```text
pustakaalay/
├── backend/
│   ├── src/main/java/com/pustakaalay/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── security/
│   │   └── service/
│   ├── src/main/resources/
│   └── pom.xml
│
├── database/
│   └── schema.sql
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── context/
│   │   ├── layouts/
│   │   ├── pages/
│   │   └── services/
│   └── package.json
│
└── README.md
