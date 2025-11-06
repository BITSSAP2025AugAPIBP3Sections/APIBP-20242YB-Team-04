# booking-service

Spring Boot microservice for managing event bookings and providing analytics via GraphQL.

## Features
- Booking entity with fields: id, eventId, userId, userName, userEmail, tickets, totalAmount, bookingDate, city, category
- REST Endpoints:
  - `POST /bookings` – Create a new booking (supports async with `?async=true`)
  - `GET /bookings` – Retrieve all bookings
  - `GET /bookings/{id}` – Get booking by ID
  - `DELETE /bookings/{id}` – Delete booking by ID
  - `GET /trending` – Demo endpoint for trending events
  - `GET /personalized` – Demo endpoint for personalized recommendations
  - `GET /stats` – Aggregated stats (legacy, replaced by GraphQL)
- GraphQL Endpoint:
  - `POST /graphql` – Execute advanced analytics and nested queries
  - Example:
    ```graphql
    query {
      eventStats(eventId: 202) {
        totalBookings
        recentBookings
        wishlistCount
        avgRating
        city
        category
        timestamp
      }
    }
    ```
- Async processing with Spring `@Async` and `ThreadPoolTaskExecutor`
- Swagger / OpenAPI documentation via Springdoc
- H2 in-memory DB for quick testing (swap to PostgreSQL/MySQL in production)

## Folder Structure
```
booking-service/
├── src/main/java
├── src/main/resources
└── Dockerfile
```

## Build & Run

### Build
```bash
mvn -U -DskipTests package
java -jar target/booking-service-1.0.0.jar

## Swagger API Documentation
Access the Swagger UI here:  
🔗 **http://localhost:8082/swagger-ui/index.html#/**

