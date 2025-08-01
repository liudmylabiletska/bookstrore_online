# 📚 Online Book Store API
A secure and extensible backend system for managing books, categories, users, and orders.  
This API handles registration, JWT authentication, partial book updates via PATCH, role-based access control, and integration testing.
It supports soft deletion using Hibernate annotations and uses Swagger for dynamic documentation.  
The project is containerized with Docker, version-controlled via Liquibase, and designed to scale in production-ready environments.
---

### 🚀 Motivation

This project showcases my backend engineering skills using Spring Boot and clean architecture.
It addresses common tasks in e-commerce systems like authentication, data filtering, validation, and role-based operations.

### Main project structure

online-book-store/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── mate/academy/
│   │   │       ├── controller/     # REST controllers
│   │   │       ├── dto/            # Data Transfer Objects
│   │   │       ├── model/          # JPA entity
│   │   │       ├── repository/     # Spring Data repositories
│   │   │       ├── service/        # Business Logic
│   │   │       └── config/         # Configuration
│   │   └── resources/
│   │       ├── db/changelog/              # Liquibase
│   │       └── application.properties     # Конфігурація Spring
│   └── test/                              # Tests
├── docker-compose.yml                     # Docker конфігурація
├── .env.template                          # Template of environment variables
├── .env                           # .env configuration (non Git)
└── pom.xml                        # Maven configuration


###  Model Diagram
[model_diagram.png](model_diagram.png)
This diagram shows the relationships between entities like `User`, `Book`, `Order`, `ShoppingCart`, etc.  
It supports understanding of the domain structure for contributors and reviewers.
Here's the diagram of all models with correct relationships:

**Main Relationships:**

User ↔ Role - Many-to-Many (through users_roles table)
User ↔ ShoppingCart - One-to-One (each user has one cart)
User ↔ Order - One-to-Many (user can have many orders)
ShoppingCart ↔ CartItem - One-to-Many (cart contains many items)
Book ↔ Category - Many-to-Many (through books_categories table)
CartItem ↔ Book - Many-to-One (cart item references a book)
Order ↔ OrderItem - One-to-Many (order contains many items)
OrderItem ↔ Book - Many-to-One (order item references a book)

**Key Features:**

All entities have soft delete through isDeleted field
ShoppingCart uses @MapsId for relationship with User
StatusName - enum for order statuses
RoleName - enum for user roles

### 🛠 Tech Stack

 Technology        | Version               |
|------------------|-----------------------|
| Java             | 17                    |
| Spring Boot      | 3.5.0                 |
| Spring Security  | 3.5.0                 |
| Spring Data JPA  | 3.5.0                 |
| Liquibase        | 4.32                  |
| Docker           | 28.2.2                |
| Swagger / OpenAPI| 2.8.8                 |
| MapStruct        | 1.5.5.Final           |
| JUnit            | 5.x(Spring Boot Test) |
| Mockito          | 5.x(Spring Boot Test) |
| MockMvc          | built-in              |
| Testcontainers   | 1.21.3                |


### 📦 Functionality

| Feature           | Description                                                                 |
|-------------------|------------------------------------------------------------------------------|
| Book Management   | List, search, create, partial update (PATCH), soft-delete                    |
| Categories        | Assign categories to books, filter by category                               |
| Authentication    | Register/login with JWT, user/admin roles                                   |
| Orders (optional) | Add books to cart, submit orders, view user history                         |
| DTO Validation    | Conditional constraints for PATCH vs POST                                   |
| Testing           | Unit + integration tests using SQL fixtures and containerized DBs            |


## 🗂 API Documentation

**Use for connection Swagger/Postman**

http://localhost:8081/api/auth/login:
"email": "dmytro@example.com",
"password": "12345678910"

Result: "token": "<your-jwt-token>"

AUTHORIZATION: Bearer <your-jwt-token>

- **Swagger UI**: [http://localhost:8081/api/swagger-ui/index.html](http://localhost:8081/api/swagger-ui/index.html#)

- **Postman Collection** http://localhost:8081/api/auth/login

  Authentication

✅ User Registration
✅ User Login (з JWT token)

Books Management

✅ Get All Books (with pagination)
✅ Get Book by ID
✅ Create Book (Admin)
✅ Update Book (Admin)
✅ Delete Book (Admin)
✅ Search Books (with filters)

Categories Management

✅ CRUD операції для категорій
✅ Get Books by Category

Shopping Cart

✅ Get Cart
✅ Add Book to Cart
✅ Update Item Quantity
✅ Remove Item from Cart

Orders Management

✅ Create Order
✅ Get User Orders
✅ Update Order Status (Admin)
✅ Get Order Items
✅ Get Orders by Status (Admin)

### 📥 How to Run Locally

**_Step 1: Clone the Repository_**:

https://github.com/kostya-savchenko/online-book-store

bash# git clone <repository-url>
cd online-book-store

**_Step 2: Set Up Environment Variables_**:
bash# Copy the environment template
cp .env.template .env

**_Step 3: Configure Your .env File_**:
Open the .env file and fill in the following variables:

# Database Configuration:

MYSQLDB_USER=root
MYSQLDB_ROOT_PASSWORD=YourSecurePassword123
MYSQLDB_DATABASE=book_store
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306

# Spring Boot Configuration
SPRING_LOCAL_PORT=8081
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
⚠️ Important:
Change MYSQLDB_ROOT_PASSWORD to your own secure password
Make sure ports don't conflict with other services

**_Step 4: Maven repackage_**:
bash# Repackage the project with command: mvn clean package

**_Step 5: Run with Docker_**:
bash# Start all services
docker-compose up --build

# Or run in background
docker-compose up --build -d

**_Step 6: Verify the Setup_**:
After successful startup, services will be available at:

API: http://localhost:8081
Swagger UI: http://localhost:8081/api/swagger-ui.html
MySQL: localhost:3307
