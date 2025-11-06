# Auth & User Service

## Overview
The **Auth & User Service** is a core microservice in the Eventix platform, responsible for handling authentication, authorization (JWT), and user profile management.  
It is built using Spring Boot, follows microservices architecture best practices, and exposes REST APIs with Swagger documentation enabled.

---

## Responsibilities
- User registration & login
- JWT token validation
- Role-based access control

---

## Swagger API Documentation
Access the Swagger UI here:  
🔗 **http://localhost:8081/swagger-ui/swagger-ui/index.html#/**

---

## Endpoints Overview
| Method | Endpoint | Description |
|--------|-----------|-------------|
| POST | /users/register | Registers a new user |
| POST | /users/login | Authenticates user & returns JWT |
| GET | /auth/validateToken | Validates JWT token |

---

## Tech Stack
- Java 17+
- Spring Boot
- REST APIs
- Swagger (OpenAPI)
- Docker & Docker Compose
- Independent DB per service

---

## How to Run the Service

###  1. Install dependencies
```bash
mvn clean install
```

###  2. Run the service
```bash
mvn spring-boot:run
```

---

## Docker Usage

### Build Docker image
```bash
docker build -t user-service .
```

### Run container
```bash
docker run -p <port>:<port> user-service
```

## Folder Structure
```
user-service/
├── src/main/java
├── src/main/resources
└── Dockerfile
```

---

## Notes
This service runs independently as part of the Eventix microservices ecosystem and communicates with other services via REST or message broker events.

