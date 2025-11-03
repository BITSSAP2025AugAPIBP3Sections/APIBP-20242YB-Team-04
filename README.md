# 🎟️ Eventix – Event Ticketing & Volunteering Platform (Microservices-Based)

## 🧩 Overview
**Eventix** is a scalable, microservices-based event ticketing and management system designed to simplify how organizers create and manage events, and how users discover and participate in volunteering opportunities, donation drives, and city-wide events.

The system provides secure authentication, event creation, ticket booking, and real-time communication between services — built to demonstrate scalability, modularity, and reliability in distributed systems.

---

## 🚀 Objectives
- **Primary Goal:** Design and deploy a microservices-based, scalable application demonstrating modern API and cloud-native practices.
- **Use Case:** Volunteering & donation event discovery and participation.
- **Focus Areas:**
  - RESTful and gRPC-based service communication
  - Scalable microservice decomposition
  - Dockerized and orchestrated deployment (Docker Compose / Kubernetes)
  - Message-driven communication using a broker (RabbitMQ)

---

## 🏗️ System Scope
Eventix supports multiple user roles — **Organizer, Attendee, and Admin** — enabling:
- Organizers to create, update, and manage events.
- Users to search, book, and participate in volunteering events.
- System to handle waitlists, notifications, and promotions at scale.

The project is deployed locally using **Docker Compose** (and optionally **Minikube**) for scalability testing and load simulation.

---

## 🧠 Problem Statement
As the number of volunteering and community events grows, organizers face challenges in managing participants, ticketing, and communication efficiently. Traditional monolithic systems struggle to scale dynamically under varying loads.

**Eventix addresses this by providing a microservices-based architecture** that scales individual components independently, ensuring reliability and performance under high demand.

---

## 💡 Proposed Solution
Eventix is developed as a **set of independent microservices**, each responsible for a specific business domain such as user management, event creation, booking, or notifications.  
Communication is implemented using:
- **REST APIs** for user-facing services
- **gRPC** for internal service-to-service calls
- **RabbitMQ** for asynchronous event updates (e.g., booking → notification)
- **API Gateway** for unified routing and authentication

Deployment uses **Docker Compose** for local scaling and **Kubernetes (Minikube)** for orchestration.

---

## 🧩 Microservices Architecture

| **Service Name** | **Responsibilities** | **Communication Type** |
|------------------|---------------------|-------------------------|
| **Auth & User Service** | User registration, authentication, JWT generation, role management | REST |
| **Event Management Service** | CRUD for events, venues, and schedules | REST |
| **Search & Discovery Service** | Event discovery by location, category, and date | REST / gRPC |
| **Booking & Order Service** | Ticket booking, order tracking, seat management | gRPC / Message Broker |
| **Promo & Loyalty Service** | Manage discounts and loyalty campaigns | REST |
| **Notification Service** | Sends emails, SMS, and in-app notifications | Message Broker (RabbitMQ) |
| **API Gateway** | Routes external requests to microservices securely | REST |

> Decomposition Choice: **By Business Domain** (Each service aligns with a core business capability — e.g., user, event, booking, notification).

---

## ⚙️ Tech Stack

### **Backend**
- **Spring Boot (Java)** – REST & gRPC service development  
- **Spring Data JPA / Hibernate** – Database persistence  
- **Spring Security (JWT)** – Authentication & role-based access  
- **RabbitMQ** – Message-based communication  
- **PostgreSQL / MySQL** – Relational database  
- **Docker & Docker Compose** – Containerization & local scaling  
- **Kubernetes (Minikube)** – Optional orchestration environment  

### **Frontend**
- **React** – Web UI for event creation and discovery  
- **Axios / Fetch** – API consumption  
- **Recharts** – Data visualization (optional for dashboard)  

---

## 🔗 Example API Endpoints

### Auth & User Service
POST /api/v1/auth/register
POST /api/v1/auth/login
GET /api/v1/users/{id}

### Event Service
POST /api/v1/events
GET /api/v1/events/{id}
GET /api/v1/events?location=Delhi&date=2025-11-05

### Booking Service
POST /api/v1/bookings
GET /api/v1/bookings/{bookingId}
DELETE /api/v1/bookings/{bookingId}/cancel

### Notification Service
POST /api/v1/notify/email
POST /api/v1/notify/inapp