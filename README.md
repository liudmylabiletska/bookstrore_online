# 📚 Online Book Store API

A secure and extensible backend system for managing books, categories, users, and orders. This API handles registration, JWT authentication, partial book updates via PATCH, role-based access control, and integration testing. It supports soft deletion using Hibernate annotations and uses Swagger for dynamic documentation. The project is containerized with Docker, version-controlled via Liquibase, and designed to scale in production-ready environments.

---

## 🇺🇸 English Version

### 🚀 Motivation

This project showcases my backend engineering skills using Spring Boot and clean architecture. It addresses common tasks in e-commerce systems like authentication, data filtering, validation, and role-based operations.

### 🛠 Tech Stack

- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- Liquibase
- Docker & Docker Compose
- Swagger / OpenAPI
- MapStruct
- JUnit / Mockito / MockMvc / Testcontainers

### 📦 Functionality

| Feature           | Description                                                                 |
|-------------------|------------------------------------------------------------------------------|
| Book Management   | List, search, create, partial update (PATCH), soft-delete                    |
| Categories        | Assign categories to books, filter by category                               |
| Authentication    | Register/login with JWT, user/admin roles                                   |
| Orders (optional) | Add books to cart, submit orders, view user history                         |
| DTO Validation    | Conditional constraints for PATCH vs POST                                   |
| Testing           | Unit + integration tests using SQL fixtures and containerized DBs            |

### 📥 How to Run Locally

https://github.com/kostya-savchenko/online-book-store
git clone https://github.com/your-username/onlin-book-store.git
cd online-book-store
docker-compose up --build
