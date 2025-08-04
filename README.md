# 📚 Online Book Store API
A secure and extensible backend system for managing books, categories, users, and orders.  
This API handles registration, JWT authentication, partial book updates via PATCH, role-based access control, and integration testing.
It supports soft deletion using Hibernate annotations and uses Swagger for dynamic documentation.  
The project is containerized with Docker, version-controlled via Liquibase, and designed to scale in production-ready environments.

## 🚀 Motivation

This project showcases my backend engineering skills using Spring Boot and clean architecture.
It addresses common tasks in e-commerce systems like authentication, data filtering, validation, and role-based operations.

## Main project structure

### Online Book Store 

```
online-book-store/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── mate/academy/
│   │   │       ├── controller/                # REST controllers
│   │   │       ├── dto/                       # Data Transfer Objects  
│   │   │       ├── model/                     # JPA entity
│   │   │       ├── repository/                # Spring Data repositories
│   │   │       ├── service/                   # Business Logic
│   │   │       └── config/                    # Configuration
│   │   └── resources/
│   │       ├── db/changelog/                  # Liquibase migrations
│   │       └── application.properties         # Configuration Spring
│   └── test/                                  # Tests
├── docker-compose.yml                         # Docker Configuration  
├── .env.template                              # Template of environment variables
├── .env                                       # .env configuration (не в Git)
└── pom.xml                                    # Maven configuration
```

### Structure Description:

#### Main Java packages:
- **controller/** - REST controllers for handling HTTP requests
- **dto/** - Data Transfer Objects for API
- **model/** - JPA entities for database operations
- **repository/** - Spring Data JPA repositories
- **service/** - Business logic layer
- **config/** - Spring configuration classes

#### Resources:
- **db/changelog/** - Database migrations via Liquibase
- **application.properties** - Main Spring Boot configuration

#### Configuration files:
- **docker-compose.yml** - Docker containers setup
- **.env.template** - Environment variables template
- **.env** - Environment variables (not tracked by Git)
- **pom.xml** - Maven dependencies and configuration

## Model Diagram

![Model Diagram](model_diagram.png)

This diagram shows the relationships between entities like `User`, `Book`, `Order`, `ShoppingCart`, etc.  
It supports understanding of the domain structure for contributors and reviewers.
This diagram shows the relationships between entities like `User`, `Book`, `Order`, `ShoppingCart`, etc.  
It supports understanding of the domain structure for contributors and reviewers.
Here's the diagram of all models with correct relationships:

### Entity Relationship

#### User Relations:
- **User ↔ Role**
  - **Many-to-Many** (through `users_roles` table)
  - One user can have multiple roles, one role can be assigned to multiple users

- **User ↔ ShoppingCart**
  - **One-to-One** (each user has one cart)
  - Each user has exactly one shopping cart

- **User ↔ Order**
  - **One-to-Many** (user can have many orders)
  - One user can place multiple orders over time

#### Shopping Cart Relations:
- **ShoppingCart ↔ CartItem**
  - **One-to-Many** (cart contains many items)
  - One shopping cart can contain multiple cart items

#### Book Relations:
- **Book ↔ Category**
  - **Many-to-Many** (through `books_categories` table)
  - One book can belong to multiple categories, one category can contain multiple books

- **CartItem ↔ Book**
  - **Many-to-One** (cart item references a book)
  - Multiple cart items can reference the same book

#### Order Relations:
- **Order ↔ OrderItem**
  - **One-to-Many** (order contains many items)
  - One order can contain multiple order items

- **OrderItem ↔ Book**
  - **Many-to-One** (order item references a book)
  - Multiple order items can reference the same book

#### Summary of Junction Tables:
- `users_roles` - Links users with their roles
- `books_categories` - Links books with their categories

### **Key Features:**

All entities have soft delete through isDeleted field
ShoppingCart uses @MapsId for relationship with User
StatusName - enum for order statuses
RoleName - enum for user roles

## 🛠 Tech Stack

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


## 📦 Functionality

| Feature           | Description                                                                 |
|-------------------|------------------------------------------------------------------------------|
| Book Management   | List, search, create, partial update (PATCH), soft-delete                    |
| Categories        | Assign categories to books, filter by category                               |
| Authentication    | Register/login with JWT, user/admin roles                                   |
| Orders (optional) | Add books to cart, submit orders, view user history                         |
| DTO Validation    | Conditional constraints for PATCH vs POST                                   |
| Testing           | Unit + integration tests using SQL fixtures and containerized DBs            |


## 🗂 API Endpoints

####   Authentication

✅ User Registration (POST: /api/auth/register)

✅ User Login (з JWT token) (POST: /api/auth/login)

#### Books Management

✅ Get All Books (with pagination) (GET: /api/books)

✅ Get Book by ID (GET: /api/books/{id})

✅ Create Book (Admin) (POST: /api/books/)

✅ Update Book (Admin) (PATCH: /api/books/{id})

✅ Delete Book (Admin) (DELETE: /api/books/{id})

✅ Search Books (with filters) (GET: /api/books/search)

#### Categories Management

✅ Get All Categories (with pagination) (GET: /api/categories)

✅ Get Book by ID (GET: /api/categories/{id})

✅ Create Category (Admin) (POST: /api/categories/)

✅ Update Category (Admin) (PATCH: /api/categories/{id})

✅ Delete Category (Admin) (DELETE: /api/categories/{id})

✅ Get Books by Category (GET: /api/categories/{id}/books)

#### Shopping Cart

✅ Get Cart (GET: /api/cart)

✅ Add Book to Cart (POST: /api/cart)

✅ Update Item Quantity (PUT: /api/cart/cart-items/{cartItemId})

✅ Remove Item from Cart (DELETE: /api/cart/cart-items/{cartItemId})

#### Orders Management

✅ Create Order (POST: /api/orders)

✅ Get User Orders (GET: /api/orders)

✅ Update Order Status (Admin) (PATCH: /api/orders/{id})

✅ Get Order Items (GET: /api/orders/{orderId}/items)

✅ Get Order Items (GET: /api/orders/{orderId}/items)

✅ Get one item by itemId from the order by orderId (GET: /api/orders/{orderId}/items/{itemId})

✅ Get Orders by Status (Admin) (GET: /api/orders/status/{status})

### Access Swagger UI
For API documentation and testing, navigate to:
```bash
http://localhost:8081/api/swagger-ui/index.html#
```
Since the project uses Spring Security, you'll need to log in with the following credentials:
**Username: dmytro@example.com**(USER, ADMIN roles)
**Password: 12345678910**

## Postman Collection

### 🔑 Authentication

#### Register a New User
- **Endpoint**: `POST /api/auth/registration`
- **Description**: Registers a new user. 
- **Example Link**: [http://localhost:8080/api/auth/registration](http://localhost:8080/api/auth/registration)
- **Request Body**:
  ```json
  {
    "email": "bob@example.com",
    "password": "12345678",
    "repeatPassword": "12345678",
    "firstName": "Bob",
    "lastName": "Alison",
    "shippingAddress": "Bob's address"
  }
  ```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body**:
  ```json
  {
    "id": 5,
    "email": "bob@example.com",
    "firstName": "Bob",
    "lastName": "Alison",
    "shippingAddress": "Bob's address"
  }
  ```
#### User Login
- **Endpoint**: `POST /api/auth/login`
- **Description**: Logs in a registered user. Accessible for all users.
- **Example Link**: [http://localhost:8080/api/auth/login](http://localhost:8080/api/auth/login)
- **Request Body**:
  ```json
  {
    "email": "bob@example.com",
    "password": "12345678"
  }
  ```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body**:
   ```json
  {
  "token": "your_jwt_token_here"
  }
  ```
### 📖 Book

#### Get All Books
- **Endpoint**: `GET /api/books`
- **Description**: Returns a list of all stored books. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/books](http://localhost:8080/api/books)
- **Response**:
  - **Status Code**: `200 OK`
  - **Body** (example):
  
```json
{
"content": [
{
"id": 2,
"title": "Triumphal arch",
"author": "Erich Maria Remarque",
"isbn": "san-126-85-02t",
"price": 700.00,
"description": "This is a sample book description.",
"coverImage": "http://example.com/cover1.jpg",
"categoryIds": [
3,
6
]
},
{
"id": 5,
"title": "One Hundred Years of Solitude",
"author": "Gabriel Garsia Marquez",
"isbn": "san-123-45-02ttt2",
"price": 800.00,
"description": "This is a sample book description2.",
"coverImage": "http://example.com/cover2.jpg",
"categoryIds": [
3,
6
]
}
],
"page": {
"size": 20,
"number": 0,
"totalElements": 2,
"totalPages": 1
}
}
  ```
#### Get Book by ID
- **Endpoint**: `GET /api/books/{id}`
- **Description**: Returns a book by the specified ID. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/books/5](http://localhost:8080/api/books/5)
- **Response**:
  - **Status Code**: `200 OK`
  - **Body** (example):
 ```json
{
  "id": 5,
  "title": "One Hundred Years of Solitude",
  "author": "Gabriel Garsia Marquez",
  "isbn": "san-123-45-02ttt2",
  "price": 800.00,
  "description": "This is a sample book description2.",
  "coverImage": "http://example.com/cover2.jpg",
  "categoryIds": [
    3,
    6
  ]
}
```
#### Search Books
- **Endpoint**: `GET /api/books/search`
- **Description**: Searches books using specified parameters. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8081/api/books/search?title=Triump](http://localhost:8081/api/books/search?title=Triump)
- **Response**:
  - **Status Code**: `200 OK`
  - **Body** (example):
```json
{
  "content": [
    {
      "id": 2,
      "title": "Triumphal arch",
      "author": "Erich Maria Remarque",
      "isbn": "san-126-85-02t",
      "price": 700.00,
      "description": "This is a sample book description.",
      "coverImage": "http://example.com/cover1.jpg",
      "categoryIds": [
        3,
        6
      ]
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```
#### Create a New Book
- **Endpoint**: `POST /api/books`
- **Description**: Creates a new book in the database. Accessible for role **Admin**. **WARNING! Before adding a book, the corresponding category must be added**
- **Example Link**: http://localhost:8080/api/books
- **Request Body**:
```json
{
  "title": "New Book",
  "author": "New Author",
  "isbn": "00000000000001",
  "price": 100.99,
  "description": "New description",
  "coverImage": "https://example.com/newbook-cover-image.jpg",
  "categoryIds": [1, 6]
}
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 6,
  "title": "New Book",
  "author": "New Author",
  "isbn": "00000000000001",
  "price": 100.99,
  "description": "New description",
  "coverImage": "https://example.com/newbook-cover-image.jpg",
  "categoryIds": [
    1,
    6
  ]
}
```
#### Update a Book
- **Endpoint**: `PATCH /api/books/{id}`
- **Description**: Updates the book with the specified ID. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/books/1](http://localhost:8080/api/books/1)
- **Request Body**:
```json
{
  "title": "Book",
  "author": "New Author",
  "isbn": "00000000000001",
  "price": 222.99,
  "description": "New description",
  "coverImage": "https://example.com/newbook-cover-image.jpg",
  "categoryIds": [1, 6]
}
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 6,
  "title": "Book",
  "author": "New Author",
  "isbn": "00000000000001",
  "price": 222.99,
  "description": "New description",
  "coverImage": "https://example.com/newbook-cover-image.jpg",
  "categoryIds": [
    1,
    6
  ]
}
```
#### Delete a Book
- **Endpoint**: `DELETE /api/books/{id}`
- **Description**: Soft-deletes a book with the specified ID from the database. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/books/1](http://localhost:8080/api/books/6)
- **Response**:
  - **Status Code**: `204 No content`

### 📜 Category

#### Create a New Category
- **Endpoint**: `POST /api/categories`
- **Description**: Creates a new category. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/categories](http://localhost:8080/api/categories)
- **Request Body**:
```json
{
  "name": "Adventures",
  "description": "Books about adventures"
}
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 8,
  "name": "Adventures",
  "description": "Books about adventures"
}
```
#### Get All Categories
- **Endpoint**: `GET /api/categories`
- **Description**: Returns a list of all categories from the database. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/categories](http://localhost:8080/api/categories)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "content": [
    {
      "id": 1,
      "name": "Science Fiction",
      "description": "Books set in futuristic or imaginative worlds"
    },
    {
      "id": 2,
      "name": "Mystery",
      "description": "Detective stories and whodunits"
    },
    {
      "id": 3,
      "name": "Biography",
      "description": "Life stories of notable individuals"
    },
    {
      "id": 4,
      "name": "Fantasy",
      "description": "Books with magic, mythical creatures, and imaginary worlds"
    },
    {
      "id": 5,
      "name": "Self-Help",
      "description": "Guides for personal growth and motivation"
    },
    {
      "id": 6,
      "name": "Drama",
      "description": "Narrative works focused on realistic characters, emotional conflict, and dramatic themes"
    },
    {
      "id": 7,
      "name": "Programming",
      "description": "Books dedicated to creating, debugging and optimizing software"
    },
    {
      "id": 8,
      "name": "Adventures",
      "description": "Books about adventures"
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 8,
    "totalPages": 1
  }
}
```
#### Get Books by Category
- **Endpoint**: `GET /api/categories/{id}/books`
- **Description**: Returns a list of books that belong to a specific category. Accessible for roles **User**.
- **Example Link**: [http://localhost:8080/api/categories/6/books](http://localhost:8080/api/categories/6/books)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "content": [
    {
      "id": 2,
      "title": "Triumphal arch",
      "author": "Erich Maria Remarque",
      "isbn": "san-126-85-02t",
      "price": 700.00,
      "description": "This is a sample book description.",
      "coverImage": "http://example.com/cover1.jpg"
    },
    {
      "id": 5,
      "title": "One Hundred Years of Solitude",
      "author": "Gabriel Garsia Marquez",
      "isbn": "san-123-45-02ttt2",
      "price": 800.00,
      "description": "This is a sample book description2.",
      "coverImage": "http://example.com/cover2.jpg"
    }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 2,
    "totalPages": 1
  }
}
```
#### Update a Category
- **Endpoint**: `PATCH /api/categories/{id}`
- **Description**: Updates the category with the specified ID. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/categories/8](http://localhost:8080/api/categories/8)
- **Request Body**:
```json
{
  "name": "Adventures",
  "description": "Updated description"
}
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 8,
  "name": "Adventures",
  "description": "Updated description"
}
```
#### Delete a Category
- **Endpoint**: `DELETE /api/categories/{id}`
- **Description**: Soft-deletes the category with the specified ID. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/categories/8](http://localhost:8080/api/categories/8)
- **Response**:
  - **Status Code**: `204 No content`

### 🧾 Order

#### Get All Orders
- **Endpoint**: `GET /api/orders`
- **Description**: Returns a list of all orders. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/orders](http://localhost:8080/api/orders)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
[
  {
    "id": 1,
    "userId": 4,
    "orderItems": [
      {
        "id": 1,
        "bookId": 2,
        "quantity": 1
      }
    ],
    "orderDate": "2025-08-04T15:33:41",
    "total": 700.00,
    "status": "pending"
  }
]
```
#### Get Order Items
- **Endpoint**: `GET /api/orders/{orderId}/items`
- **Description**: Returns a list of items in a specific order. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/orders/1/items](http://localhost:8080/api/orders/1/items)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
[
  {
    "id": 1,
    "bookId": 2,
    "quantity": 1
  }
]
```
#### Get Specific Order Item
- **Endpoint**: `GET /api/orders/{orderId}/items/{itemId}`
- **Description**: Returns a specific item from a specific order. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/orders/1/items/1](http://localhost:8080/api/orders/1/items/1)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 1,
  "bookId": 2,
  "quantity": 1
}
```
#### Create a New Order
- **Endpoint**: `POST /api/orders`
- **Description**: Creates a new order. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/orders](http://localhost:8080/api/orders)
- **Request Body**:
```json
{
  "shippingAddress": "New address"
}
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 1,
  "userId": 4,
  "orderItems": [
    {
      "id": 1,
      "bookId": 2,
      "quantity": 1
    }
  ],
  "orderDate": "2025-08-04T15:33:41",
  "total": 700.00,
  "status": "pending"
}
```
#### Update Order Status
- **Endpoint**: `PATCH /api/orders/{id}`
- **Description**: Updates the status of an order. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/orders/1](http://localhost:8080/api/orders/1)
- **Request Body**:
```json
  {
    "status": "completed"
  }
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 1,
  "userId": 4,
  "orderItems": [
    {
      "id": 1,
      "bookId": 2,
      "quantity": 1
    }
  ],
  "orderDate": "2025-08-04T15:33:41",
  "total": 700.00,
  "status": "completed"
}
```
### 🛒 Shopping Cart

#### Get Shopping Cart
- **Endpoint**: `GET /api/cart`
- **Description**: Returns the shopping cart of the logged-in user.
- **Example Link**: [http://localhost:8080/api/cart](http://localhost:8080/api/cart)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 4,
  "userId": 4,
  "cartItems": [
    {
      "id": 1,
      "bookId": 5,
      "bookTitle": "One Hundred Years of Solitude",
      "quantity": 4
    }
  ]
}
```
#### Add Book to Shopping Cart
- **Endpoint**: `POST /api/cart`
- **Description**: Adds a book to the shopping cart of the logged-in user.
- **Example Link**: [http://localhost:8080/api/cart](http://localhost:8080/api/cart)
- **Request Body**:
```json
[
  {
  "bookId": 5,
  "quantity": 4
  }
]
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 4,
  "userId": 4,
  "cartItems": [
    {
      "id": 1,
      "bookId": 5,
      "bookTitle": "One Hundred Years of Solitude",
      "quantity": 4
    }
  ]
}
```
#### Update Cart Item Quantity
- **Endpoint**: `PUT /api/cart/cart-items/{itemId}`
- **Description**: Changes the quantity of a cart item with the specified ID.
- **Example Link**: [http://localhost:8080/api/cart/cart-items/1](http://localhost:8080/api/cart-items/items/1)
- **Request Body**:
```json
{
  "quantity": 1
}
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 4,
  "userId": 4,
  "cartItems": [
    {
      "id": 1,
      "bookId": 5,
      "bookTitle": "One Hundred Years of Solitude",
      "quantity": 1
    }
  ]
}
```
#### Delete Cart Item
- **Endpoint**: `DELETE /api/cart/cart-items/{itemId}`
- **Description**: Soft-deletes a cart item with the specified ID.
- **Example Link**: [http://localhost:8080/api/cart/cart-items/1](http://localhost:8080/api/cart-items/1)
- **Response**:
  - **Status Code**: `204 No content`


## 📥 How to Run Locally

#### **_Step 1: Clone the Repository_**:

https://github.com/kostya-savchenko/online-book-store

```bash
git clone https://github.com/kostya-savchenko/online-book-store.git
cd online-book-store
```

#### **_Step 2: Set Up Environment Variables_**:

Copy the environment template
```bash
cp .env.template .env
```

#### **_Step 3: Configure Your .env File_**:

Open the .env file and fill in the following variables:

**Database Configuration:**
```
MYSQLDB_USER=root

MYSQLDB_ROOT_PASSWORD=your_secure_password

MYSQLDB_DATABASE=book_store

MYSQLDB_LOCAL_PORT=3307

MYSQLDB_DOCKER_PORT=3306
```

**Spring Boot Configuration**
```
SPRING_LOCAL_PORT=8081

SPRING_DOCKER_PORT=8080

DEBUG_PORT=5005
```
**⚠️ Important:**
Change MYSQLDB_ROOT_PASSWORD to your own secure password
Make sure ports don't conflict with other services

#### **_Step 4: Maven repackage_**:
Repackage the project with command:
```bash
mvn clean package
```

#### **_Step 5: Run with Docker_**:

Start all services
```bash
docker-compose up --build
```

#### **_Step 6: Verify the Setup_**:

After successful startup, services will be available at:

API: http://localhost:8081

Swagger UI: http://localhost:8081/api/swagger-ui.html

MySQL: localhost:3307
