
<div align="center">

# 📚 Pustakaalay

### Smart Library Management System

A full-stack web application for managing books, physical copies, library members, borrowings, returns and fines.

<br>

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![MySQL](https://img.shields.io/badge/MySQL-8.4-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

![JWT](https://img.shields.io/badge/Auth-JWT-black?style=flat-square&logo=jsonwebtokens)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Backend-Render-46E3B7?style=flat-square&logo=render)
![Aiven](https://img.shields.io/badge/Database-Aiven-FF3554?style=flat-square)
![Vercel](https://img.shields.io/badge/Frontend-Vercel-black?style=flat-square&logo=vercel)

<br>

[![Backend](https://img.shields.io/badge/Backend-Live-success?style=for-the-badge)](https://pustakaalay-backend.onrender.com/api/health)
[![Source](https://img.shields.io/badge/Source-GitHub-181717?style=for-the-badge&logo=github)](https://github.com/iamshashwaat/Pustakaalay)

</div>

---

# 🚀 How to Use Pustakaalay

Pustakaalay has two roles:

- **ADMIN** - manages the complete library
- **MEMBER** - browses books and views personal records

## 🔑 Demo Login

### 🧑‍💼 Admin

Email: `admin@pustakaalay.local`  
Password: `Admin@123`

Admin can:

- Add and edit books
- Add physical copies
- Manage barcodes, price, condition and shelf location
- View users
- Issue books
- Return books
- View all borrowings
- Process overdue records
- View all fines
- Mark fines as paid
- Waive fines

### 👤 Tanya Singh

Email: `tanya@pustakaalay.local`  
Password: `Member@123`

### 👤 Akarsh Srivastav

Email: `akarsh@pustakaalay.local`  
Password: `Member@123`

Members can:

- Browse books
- Search books
- Check physical copies
- View their own borrowings
- Check due dates
- View their own fines

> Members cannot view another member's borrowing or fine records.

---

# 📖 About the Project

**Pustakaalay** is a full-stack Smart Library Management System.

It manages the main library workflow:

`Books → Physical Copies → Members → Borrowings → Returns → Overdue → Fines`

The frontend is built with React.

The backend is built with Java and Spring Boot.

MySQL stores the application data.

JWT authentication and Spring Security protect the application.

---

# ✨ Main Features

## 📚 Book Management

- Add books
- Edit book details
- Search books
- Browse catalogue
- Manage authors and categories

## 📦 Physical Copy Management

A single book can have multiple physical copies.

Example:

`Atomic Habits`

- `AH-001` → AVAILABLE
- `AH-002` → BORROWED
- `AH-003` → AVAILABLE

Each copy can have:

- Barcode
- Price
- Acquisition date
- Condition
- Shelf location
- Availability status

## 🔄 Borrowing Management

- Issue books
- Return books
- Set due dates
- View active borrowings
- View returned borrowings
- Detect overdue books

## 💸 Fine Management

- Generate overdue fines
- View fine amount
- Track fine status
- Mark fines as paid
- Waive fines

## 👥 User Management

- ADMIN and MEMBER roles
- Membership numbers
- User status
- Role-based access

---

# 🔐 Security

Pustakaalay uses:

- JWT authentication
- BCrypt password hashing
- Spring Security
- Role-based authorization
- Protected REST APIs
- Protected frontend routes
- Environment variables for secrets
- Member-specific data access

## Access Rules

| Feature | ADMIN | MEMBER |
|---|:---:|:---:|
| View books | ✅ | ✅ |
| Search books | ✅ | ✅ |
| View copies | ✅ | ✅ |
| Add/Edit books | ✅ | ❌ |
| Manage copies | ✅ | ❌ |
| View users | ✅ | ❌ |
| Issue books | ✅ | ❌ |
| Return books | ✅ | ❌ |
| View all borrowings | ✅ | ❌ |
| View own borrowings | ✅ | ✅ |
| View all fines | ✅ | ❌ |
| View own fines | ✅ | ✅ |
| Mark fine paid | ✅ | ❌ |
| Waive fine | ✅ | ❌ |

---

# 🛠️ Technology Stack

## Backend

- Java 21
- Spring Boot 4.1
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Spring Security
- JWT
- BCrypt
- Maven

## Frontend

- React 19
- React Router
- Vite
- JavaScript
- CSS

## Database

- MySQL 8.4

## Deployment

- Render
- Aiven
- Vercel
- Docker
- GitHub

---

# 🏗️ Architecture

```text
User / Admin
     |
     v
React + Vite
     |
     | REST API + JWT
     v
Spring Boot
     |
     | JPA / JDBC
     v
MySQL 8.4
```
# 🗄️ Database

Pustakaalay uses **MySQL 8.4** as its relational database.

The database is designed to manage users, books, physical book copies, borrowing records, fines and other library features.

## Main Tables

```text
users
roles
books
authors
categories
book_authors
book_categories
book_copies
borrowings
fines
reservations
reviews
wishlists
notifications
```

## Main Database Relationship

```text
USER
 │
 └────── BORROWING ────── FINE
             │
             ▼
         BOOK COPY
             │
             ▼
            BOOK
          /      \
     AUTHORS    CATEGORIES
```

A **Book** stores information about a title, while **Book Copy** represents the actual physical copies available in the library.

For example:

```text
Atomic Habits
│
├── AH-001 → AVAILABLE
├── AH-002 → BORROWED
└── AH-003 → AVAILABLE
```

This allows each physical copy to have its own barcode, condition, location and availability status.

---

# 🌐 REST API

The backend follows REST API architecture.

Base API path:

```text
/api
```

## Main Endpoints

| Endpoint | Purpose |
|---|---|
| `/api/auth` | Login and authentication |
| `/api/books` | Book management |
| `/api/book-copies` | Physical book copies |
| `/api/borrowings` | Issue and return records |
| `/api/fines` | Fine management |
| `/api/users` | User management |
| `/api/roles` | Role management |
| `/api/reservations` | Reservation management |
| `/api/health` | Backend health check |

Protected API requests require a JWT token:

```text
Authorization: Bearer <JWT_TOKEN>
```

## Live Backend

```text
https://pustakaalay-backend.onrender.com
```

Health check:

```text
https://pustakaalay-backend.onrender.com/api/health
```

---

# 🔑 Authentication Flow

```text
Email + Password
       │
       ▼
POST /api/auth/login
       │
       ▼
Spring Security
       │
       ▼
BCrypt Password Verification
       │
       ▼
JWT Token Generated
       │
       ▼
React Frontend
       │
       ▼
Authorization: Bearer <JWT>
       │
       ▼
JwtAuthenticationFilter
       │
       ▼
Protected REST API
```

JWT authentication keeps the backend stateless.

The user's role is used to decide which API operations are allowed.

---

# 📂 Project Structure

```text
Pustakaalay/
│
├── backend/
│   │
│   ├── src/main/java/com/pustakaalay/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── security/
│   │   └── service/
│   │
│   ├── src/main/resources/
│   ├── Dockerfile
│   └── pom.xml
│
├── database/
│   └── schema.sql
│
├── frontend/
│   │
│   ├── src/
│   │   ├── components/
│   │   ├── context/
│   │   ├── layouts/
│   │   ├── pages/
│   │   └── services/
│   │
│   └── package.json
│
└── README.md
```

# 📌 Project Status

| Module | Status |
|---|:---:|
| React Frontend | ✅ |
| Spring Boot Backend | ✅ |
| MySQL Database | ✅ |
| JWT Authentication | ✅ |
| ADMIN Role | ✅ |
| MEMBER Role | ✅ |
| Book Catalogue | ✅ |
| Book Search | ✅ |
| Book Creation | ✅ |
| Book Editing | ✅ |
| Physical Copy Management | ✅ |
| User Management | ✅ |
| Book Issue | ✅ |
| Book Return | ✅ |
| Borrowing History | ✅ |
| Overdue Processing | ✅ |
| Fine Management | ✅ |
| Member Data Privacy | ✅ |
| Docker Support | ✅ |
| Cloud Backend | ✅ |
| Cloud Database | ✅ |

---

# 🔮 Future Improvements

Some features that can be added in future versions:

- 📧 Email notifications
- 🔔 Due-date reminders
- 🖼️ Book cover images
- 🔍 Advanced search and filters
- 📊 Dashboard charts
- 📈 Library reports
- 📖 Reservation interface
- ⭐ Reviews and ratings
- ❤️ Wishlist interface
- 🧪 More automated tests

---

# 👨‍💻 Developer

**Shashwat Srivastav**

B.Tech - Electronics and Communication Engineering  
United College of Engineering and Research, Prayagraj

GitHub: **iamshashwaat**

---

<div align="center">

## 📚 Pustakaalay

**A simple and secure way to manage a modern library.**

Built with **Java ☕ • Spring Boot 🍃 • React ⚛️ • MySQL 🐬**

</div>
