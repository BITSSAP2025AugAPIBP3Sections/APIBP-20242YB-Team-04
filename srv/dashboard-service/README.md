# Organizer Dashboard Service

## Overview
The **Organizer Dashboard Service** is a core microservice in the Eventix platform, responsible for aggregating data for organizer analytics and reporting.  
It is built using Spring Boot, follows microservices architecture best practices, and exposes REST APIs with Swagger documentation enabled.

---

## Responsibilities
- Event analytics
- Attendee insights
- Performance metrics

---

## Swagger API Documentation
Access the Swagger UI here:  
🔗 **http://localhost:8085/swagger-ui.html**

---

## Endpoints Overview
| Method | Endpoint | Description |
|--------|-----------|-------------|
| GET | /dashboard/overview | Overview metrics |
| GET | /dashboard/attendees/{eventId} | Attendee list |

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

### ✅ 1. Install dependencies
```bash
mvn clean install
```

### ✅ 2. Run the service
```bash
mvn spring-boot:run
```

---

## Docker Usage

### Build Docker image
```bash
docker build -t dashboard-service .
```

### Run container
```bash
docker run -p <port>:<port> dashboard-service
```

---

## Folder Structure
```
dashboard-service/
├── src/main/java
├── src/main/resources
└── Dockerfile
```

---

## Notes
This service runs independently as part of the Eventix microservices ecosystem and communicates with other services via REST or message broker events.

