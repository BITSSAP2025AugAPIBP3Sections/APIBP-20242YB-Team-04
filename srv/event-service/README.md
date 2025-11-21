# Event Management Service

## Overview
The **Event Management Service** is a core microservice in the Eventix platform, responsible for creating, updating, and managing events and metadata.  
It is built using Spring Boot, follows microservices architecture best practices, and exposes REST APIs with Swagger documentation enabled.

---

## Responsibilities
- Event creation
- Update event details
- Retrieve event metadata

---

## Swagger API Documentation
Access the Swagger UI here:  
🔗 **http://localhost:8080/swagger-ui/index.html**

**Alternative URLs:**
- Swagger UI: http://localhost:8080/swagger-ui.html (redirects to above)
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml

---

## Endpoints Overview
| Method | Endpoint | Description |
|--------|-----------|-------------|
| POST | /events | Create event |
| PUT | /events/{id} | Update event |
| GET | /events/{id} | Get event details |

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
docker build -t event-service .
```

### Run container
```bash
docker run -p <port>:<port> event-service
```

---

## Folder Structure
```
event-service/
├── src/main/java
├── src/main/resources
└── Dockerfile
```

---

## Notes
This service runs independently as part of the Eventix microservices ecosystem and communicates with other services via REST or message broker events.

