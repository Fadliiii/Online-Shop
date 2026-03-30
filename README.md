# Shopme Admin System

Admin management system for an e-commerce platform built with Java 17, Spring Boot, and Spring MVC.

This project demonstrates a hybrid architecture combining:

Server-side rendering (Thymeleaf)
Partial REST API (AJAX-based interactions)

Designed to simulate a real-world admin panel with authentication, role management, and scalable data handling.

## Preview

![dashboard](screenshots/dashboard.png)

![users](screenshots/users.png)

---

## Tech Stack

Backend

* Java 17
* Spring Boot 3
* Spring MVC
* Spring Data JPA
* Hibernate
* Spring Security(JWT)

Frontend

* Thymeleaf
* Bootstrap
* jQuery

Database

* MySQL

Build Tool

* Maven

---

## Features

⚙️ Core Features
🔐 Authentication & Authorization
Login system with JWT token
Role-based access control (Admin/User roles)
👥 User Management
Create, update, enable/disable users
Assign roles dynamically
🗂️ Category Management
Hierarchical categories
Image upload support
🏷️ Brand Management
Associate brands with categories
Full CRUD operations
📦 Product Management
Add/edit products
Upload multiple product images
Assign categories & brands
Pagination + filtering support---

## Backend Architecture

This project uses a typical **Spring MVC layered architecture**:

Controller
Handles HTTP requests and returns Thymeleaf views.

Service
Contains business logic.

Repository
Handles database operations using Spring Data JPA.

Entity
Represents database tables using JPA annotations.

---

## Example Controller

Example controller method for listing users:

```java
@GetMapping("/users")
public String listFirstPage(Model model) {
    List<User> listUsers = userService.listAll();
    model.addAttribute("listUsers", listUsers);
    return "users/users";
}
```

This controller retrieves users from the service layer and renders the **Thymeleaf view**.

---

## Project Structure

src/main/java/com/shopme/admin

controller
Handles web requests

service
Business logic layer

repository
Spring Data JPA repositories

entity
Database entities

security
Spring Security configuration

---

## Database

Main entities used in this project:

User
Category
Brand
Product
Role

These entities are mapped to database tables using **JPA / Hibernate**.

---

## How to Run

### 1 Clone repository

git clone https://github.com/yourusername/shopme-admin

### 2 Configure database

Edit `application.properties`

spring.datasource.url=jdbc:mysql://localhost:3306/shopme
spring.datasource.username=root
spring.datasource.password=yourpassword

### 3 Run application

mvn spring-boot:run

Application will run at:

http://localhost:8080/ShopmeAdmin

---

## What I Learned

Through this project I learned:

* Building web applications using **Spring MVC**
* Server-side rendering with **Thymeleaf**
* Database persistence using **Spring Data JPA**
* Implementing authentication with **Spring Security**
* Structuring a layered Spring Boot application

---

## Future Improvements

* Convert admin features into REST APIs
* Implement DTO pattern
* Add unit testing
* Containerize the application with Docker

---

## Author

Fadli
Junior Backend Developer (Java Spring Boot)

