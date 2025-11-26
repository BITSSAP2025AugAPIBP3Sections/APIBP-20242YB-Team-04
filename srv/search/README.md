# Search & Discovery Service

## Overview
The **Search & Discovery Service** is a core microservice in the Eventix platform, responsible for indexing and searching events for discovery.  
It is built using Spring Boot, follows microservices architecture best practices, and exposes REST APIs with Swagger documentation enabled.

---

## Responsibilities
- Search by filters
- Indexing events
- Nearby event search

---

## Swagger API Documentation
Access the Swagger UI here:  
🔗 **http://localhost:8084/swagger-ui/index.html**

---

## Endpoints Overview
| Method | Endpoint | Description |
|--------|-----------|-------------|
| GET | /search | Search events |
| GET | /search/nearby | Search events by coordinates |
| POST | /index/event | Index event |

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
docker build -t search .
```

### Run container
```bash
docker run -p <port>:<port> search
```

---

## Folder Structure
```
search/
├── src/main/java
├── src/main/resources
└── Dockerfile
```

---

## Notes
This service runs independently as part of the Eventix microservices ecosystem and communicates with other services via REST or message broker events.

