# 🎟️ Eventix – Event Ticketing & Volunteering Platform (Microservices-Based)

[![CI Pipeline](https://github.com/BITSSAP2025AugAPIBP3Sections/APIBP-20242YB-Team-04/actions/workflows/ci.yml/badge.svg)](https://github.com/BITSSAP2025AugAPIBP3Sections/APIBP-20242YB-Team-04/actions/workflows/ci.yml)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.4-brightgreen)

## Overview

**Eventix** is a distributed, microservices-based event management and ticketing platform designed for community-driven volunteering and donation events.  
It enables organizers, attendees, and administrators to efficiently create, discover, manage, and participate in local events, ensuring scalability, modularity, and fault isolation.

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.4-brightgreen)
![GraphQL](https://img.shields.io/badge/GraphQL-Enabled-purple)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

---

## Problem Statement

Modern community initiatives — such as donation drives and volunteering events — often operate on fragmented or manual systems, making event discovery, registration, and coordination inefficient.  
Traditional monolithic systems struggle with scalability and performance under concurrent usage.

**Objective:**  
To design and implement a **scalable microservices-based system** that supports large-scale event discovery, user participation, and real-time updates with high reliability and performance.

---

## Key Actors

| Actor        | Description                                                                 |
|---------------|------------------------------------------------------------------------------|
| **Organizer** | Creates and manages events, schedules, and campaigns.                        |
| **Attendee**  | Searches, registers, and participates in community or donation events.       |
| **Administrator** | Oversees platform operations, manages user roles, and monitors system health. |

---

## System Scope

The system supports three primary user roles — **Organizer**, **Attendee**, and **Admin** — with the following key functionalities:

- User registration, authentication, and role-based access control (RBAC)
- Event creation, update, and management
- Event search and discovery by city, date, and category
- Ticket booking and order tracking
- Waitlist management for full events
- Notifications for bookings, cancellations, and updates
- Organizer dashboard for analytics and insights

---

## System Architecture

Eventix adopts a **business-capability-based microservice decomposition**, where each service handles a specific domain responsibility.  
All services are **containerized** using Docker and communicate via **REST/gRPC** and **asynchronous messaging** through a message broker.

### Core Microservices

| Service Name              | Primary Responsibilities                                    | Key Features |
|----------------------------|-------------------------------------------------------------|--------------|
| **Auth & User Service**    | Manages authentication (JWT), user registration, RBAC.      | User & Role Management |
| **Event Management Service** | Allows event creation, updates, and metadata management. | Event Creation & Management |
| **Search & Discovery Service** | Enables users to search events by filters and location. | Event Search & Discovery |
| **Booking & Registration Service** | Handles bookings, attendee registration, and waitlists. | Booking & Orders |
| **Promo & Loyalty Service** | Manages promo codes, discounts, and loyalty programs. | Promotions & Rewards |
| **Notification Service** | Dispatches real-time notifications and alerts. | Email/SMS/Event Notifications |
| **Organizer Dashboard Service** | Provides analytics and insights for organizers. | Event Statistics & Reports |

---

## Technology Stack

| Component | Technology |
|------------|-------------|
| **API Protocols** | REST, GraphQL |
| **Containerization** | Docker, Docker Compose |
| **Service Communication** | Message Broker (e.g., RabbitMQ / Kafka) |
| **Gateway / Load Balancer** | NGINX |
| **Databases** | Independent per service (e.g., PostgreSQL, MongoDB) |
| **Authentication** | JWT Tokens |

---

## Deployment and Scalability

- Each service is containerized and deployed using **Docker Compose**.
- **NGINX Reverse Proxy** distributes traffic and enables load balancing.
- Horizontal scaling can be demonstrated by running multiple replicas per service.
- The system is designed for future migration to **Kubernetes** or **cloud-native deployment**.

---

## Notification Flow

Booking service emits a notification event after successful persistence; RabbitMQ buffers and decouples; notification service consumes asynchronously and delivers email with retry/requeue semantics for resilience.

### Architecture Flow

1. **Booking Creation**

   * Client calls **POST /bookings**.
   * BookingService saves the booking (status = CONFIRMED).
   * A lightweight notification event (DTO) is published to RabbitMQ.

2. **Message Publishing**

   * Event sent to a **topic exchange** with a routing key (e.g., `booking.created`).
   * Routed to a **durable queue** (`booking.notifications.q`).

3. **Queue Behavior**

   * Durable (survives restarts).
   * **At-least-once** delivery → consumers must handle duplicates.
   * Optional **DLQ** for failed messages.

4. **Notification Service Consumption**

   * `@RabbitListener` consumes messages.
   * Calls EmailService to send email.
   * Success → auto-ack.
   * Failure → message requeue or DLQ (based on config).

5. **Why This Architecture**

   * Booking service is **decoupled** from email sending.
   * Allows independent scaling.
   * DTO contract ensures safe evolution.

---

## API Overview

Each microservice exposes its own set of REST or GraphQL endpoints. Below are examples of key operations for each service:

### 1. Auth & User Service
| Endpoint | Method | Description |
|-----------|---------|-------------|
| `/users/register` | POST | Registers a new user (organizer or attendee) |
| `/users/login` | POST | Authenticates a user and issues a JWT token |
| `/auth/validateToken` | GET | Validates authentication token for other services |

### 2. Event Management Service
| Endpoint | Method | Description |
|-----------|---------|-------------|
| `/events` | POST | Creates a new event (organizer-only) |
| `/events/{id}` | PUT | Updates event details |
| `/events/{id}` | DELETE | Deletes or deactivates an event |
| `/events/{id}` | GET | Retrieves event details |

### 3. Search & Discovery Service
| Endpoint | Method | Description |
|-----------|---------|-------------|
| `/search` | GET | Searches events by city, date, or category |
| `/search/nearby` | GET | Finds events near specified coordinates |
| `/search/map` | GET | Finds events according to specified coordinates |

### 4. Booking & Registration Service
| Endpoint | Method | Description |
|-----------|---------|-------------|
| `/booking` | POST | Books an event ticket or registers a participant |
| `/booking/{id}` | GET | Retrieves booking details |
| `/booking/{id}` | DELETE | Cancels a booking and triggers notifications |

### 5. Promo & Loyalty Service
| Endpoint | Method | Description |
|-----------|---------|-------------|
| `/promo/create` | POST | Creates a new promo code |
| `/promo/validate` | POST | Validates a promo code for discounts |
| `/promo/active` | GET | Lists all active promotions |

### 6. Notification Service
Triggered by events from other services (via message broker):
- `sendBookingConfirmation`
- `sendEventUpdate`
- `sendWaitlistNotification`

### 7. Organizer Dashboard Service
| Endpoint | Method | Description |
|-----------|---------|-------------|
| `/dashboard/overview` | GET | Aggregates booking and event statistics |
| `/dashboard/attendees/{eventId}` | GET | Lists all attendees for a specific event |

---

## Project Setup

### 1. Clone the Repository
```bash
git clone https://github.com/BITSSAP2025AugAPIBP3Sections/APIBP-20242YB-Team-04.git
cd APIBP-20242YB-Team-04
```
### 2. Build the Project
```bash
cd srv
mvn clean install
```
### 3. Run All Services Together

You can use the included **run.sh** script to start all microservices automatically.

##### 1. Make the Script Executable
```bash
chmod +x run.sh
```
##### 2. Run the Script
```bash
./run.sh
```
This will start:

- User Service → http://localhost:8081
- Booking Service → http://localhost:8082
- Event Service → http://localhost:8083
- Search Service → http://localhost:8084
- Dashboard Service → http://localhost:8085
- Gateway → http://localhost:8080

After ~10 seconds, visit:

-http://localhost:8080/swagger-ui.html

to access the unified Swagger interface for all microservices.

---

## Docker Deployment

### Running Dashboard Service from Docker Hub

**Prerequisites:**

- Docker Desktop installed
- Your local RabbitMQ, booking-service, and event-service must be reachable on the host

#### Step 1: Pull the Docker Image
```bash
docker pull sivaramram/dashboard-service:latest
```

#### Step 2: Run the Container
```bash
docker run -d --name dashboard-service \
  -p 8085:8085 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e RABBITMQ_HOST=host.docker.internal \
  -e RABBITMQ_PORT=5672 \
  -e BOOKING_SERVICE_URL=http://host.docker.internal:8082 \
  -e EVENT_SERVICE_URL=http://host.docker.internal:8081 \
  sivaramram/dashboard-service:latest
```

#### Step 3: Stop and Clean Up
```bash
# Stop the container
docker stop dashboard-service

# Remove the container
docker rm dashboard-service
```

---

## 📚 Documentation

- [Architecture Overview](docs/architecture/ARCHITECTURE.md)
- [Contributing Guidelines](docs/CONTRIBUTING.md)
- [Code of Conduct](docs/CODE_OF_CONDUCT.md)
- [Project Roadmap](docs/ROADMAP.md)
- [RabbitMQ Setup](docs/RABBITMQ_SETUP.md)
- [Maintainers](docs/MAINTAINERS.md)

---

## 🤝 Contributing

We welcome contributions! Please read our [Contributing Guidelines](docs/CONTRIBUTING.md) and [Code of Conduct](docs/CODE_OF_CONDUCT.md) before submitting pull requests.

### How to Contribute
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Team

See [MAINTAINERS.md](docs/MAINTAINERS.md) for the list of project maintainers and contributors.

---

## 🙏 Acknowledgments

- Built as part of the Open Source Software Development course at BITS Pilani
- Special thanks to all contributors who have helped shape this project
- Community event organizers who provided valuable feedback

---

## 🔒 Security

If you discover a security vulnerability, please do not open a public issue. Instead, report it privately to the maintainers listed in [MAINTAINERS.md](docs/MAINTAINERS.md). We take security issues seriously and will respond promptly.

---

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/BITSSAP2025AugAPIBP3Sections/APIBP-20242YB-Team-04/issues)
- **Discussions**: [GitHub Discussions](https://github.com/BITSSAP2025AugAPIBP3Sections/APIBP-20242YB-Team-04/discussions)
