# CRUD API with TDD

A RESTful Task Management API built with **Spring Boot** using **Test-Driven Development (TDD)**.

## Tech Stack

- Java 17
- Spring Boot 4.0
- Spring Data JPA
- H2 Database (in-memory)
- JUnit 5 + Mockito + AssertJ

## API Endpoints

| Method | Endpoint          | Description   | Status |
| ------ | ----------------- | ------------- | ------ |
| POST   | `/api/tasks`      | Create a task | 201    |
| GET    | `/api/tasks/{id}` | Get a task    | 200    |
| PUT    | `/api/tasks/{id}` | Update a task | 200    |
| DELETE | `/api/tasks/{id}` | Delete a task | 204    |

## Quick Start

```bash
# Clone
git clone https://github.com/Gamze0309/tdd-crud-application.git
cd tdd-crud-application

# Run
./mvnw spring-boot:run

# Test
./mvnw test
```

## Example Request

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "My Task", "description": "Task description", "dueDate": "2024-12-25"}'
```

## H2 Console

Access database at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: _(empty)_

## Project Structure

```
src/
├── main/java/com/gamze/tdd_crud/
│   ├── controller/    # REST endpoints
│   ├── service/       # Business logic
│   ├── repository/    # Data access
│   ├── entity/        # JPA entities
│   └── exception/     # Error handling
└── test/java/com/gamze/tdd_crud/
    ├── controller/    # Controller unit tests
    ├── service/       # Service unit tests
    └── integration/   # Integration tests
```
