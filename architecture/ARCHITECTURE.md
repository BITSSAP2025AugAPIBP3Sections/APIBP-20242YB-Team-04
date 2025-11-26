# Eventix Platform Architecture

## 1. Overview

Eventix is an **event management platform** that allows:

- **Attendees** to browse events and book tickets.
- **Organizers** to create events and view analytics.

The platform is implemented as a set of microservices exposed through an **API Gateway**, with asynchronous communication via **RabbitMQ** and email notifications through **SendGrid**.

---

## 2. Actors and Entry Points

### 2.1 Primary Users

- **Attendee**
  - Browse events
  - Search events
  - Book tickets

- **Organizer**
  - Create and manage events
  - View analytics and dashboards

### 2.2 Client Applications

- **Web Browser / Mobile App**
  - Single Page Application (SPA) consuming REST APIs via the API Gateway.
- **Static Content**
  - HTML/CSS/JS assets served to the browser.

All client traffic terminates at the **API Gateway**.

---

## 3. High-Level System Context

At the highest level, the **Eventix Platform (Software System)** sits between users and external systems:

- Users access the platform through:
  - **Web Browser / Mobile App**
- The platform depends on external systems:
  - **SendGrid Email API** – for transactional and notification emails.
  - **RabbitMQ Message Broker** – for asynchronous messaging (bookings, notifications, analytics events).

---

## 4. Services and Responsibilities

Below the API Gateway, Eventix is decomposed into multiple services.

### 4.1 API Gateway

- Entry point for all client requests.
- Routes calls to the appropriate backend service.
- Can host cross-cutting concerns (auth, rate limiting, logging, etc.).

### 4.2 Core Microservices

Ports from the component diagram are noted for clarity.

- **User Service (`:8081`)**
  - Manages user accounts and profiles.
  - Distinguishes between **Attendees** and **Organizers**.
  - **Validates organizers** when they perform organizer-specific actions.
  - Provides **user interests** to other services (e.g., search personalization).

- **Event Service (`:8083`)**
  - Manages event lifecycle (create, update, publish events).
  - Exposes APIs to **search events** and fetch event details.
  - Collaborates with Booking Service for availability and booking logic.

- **Search Service (`:8084`)**
  - Indexes events and possibly user behavior.
  - Provides:
    - **Search queries** over events.
    - **Trending stats** back to User Service (for recommendations / dashboards).
  - Uses data such as user interests and booking data.

- **Booking Service (`:8082`)**
  - Handles **ticket booking** flows.
  - Validates booking requests with Event Service (availability, pricing, etc.).
  - On successful booking, **publishes booking messages** to RabbitMQ.
  - Acts as the primary source of truth for bookings.

- **Dashboard Service (`:8085`)**
  - Consumes booking and analytics events from RabbitMQ.
  - Aggregates statistics for organizers:
    - Number of bookings
    - Revenue
    - Trending events / categories
  - Provides dashboards and reporting APIs.

- **Notification Service** (from system diagram)
  - Listens to domain events (e.g., booking confirmed, event updated).
  - Orchestrates email notifications via SendGrid.
  - May also interact with RabbitMQ for asynchronous processing.

---

## 5. Messaging and External Integrations

### 5.1 RabbitMQ Message Broker (`:5672`)

- Central asynchronous backbone of the system.
- **Producers**
  - **Booking Service** – publishes booking events.
- **Consumers**
  - **Dashboard Service** – uses booking events for aggregation.
  - **Event / Booking related services** – update internal state if needed.
  - **Notification Service** – triggers email/SMS/etc. on booking or event events.

This decouples real-time user flows (booking confirmation) from slower analytics and notification processing.

### 5.2 SendGrid Email API

- Used by Notification Service to send:
  - Booking confirmation emails.
  - Organizer notifications (e.g., low sales, event reminders).
- Communication is typically outbound HTTP calls from Notification Service to SendGrid.

---

## 6. Typical Interaction Flows

### 6.1 Browse & Search Events (Attendee)

1. Attendee opens the **Web UI SPA**.
2. SPA calls **API Gateway**.
3. API Gateway routes:
   - To **Search Service** for event search.
   - To **Event Service** for detailed event information.
4. Search Service may use:
   - **User Service** for user interests.
   - **Trending stats** to influence results.

### 6.2 Create & Manage Events (Organizer)

1. Organizer logs in via SPA → **API Gateway** → **User Service**.
2. Organizer creates or updates events via **API Gateway** → **Event Service**.
3. Event Service persists event data and makes them available for:
   - **Search Service** (indexing/search).
   - **Booking Service** (availability & pricing).

### 6.3 Book Tickets

1. Attendee selects an event in the SPA.
2. SPA sends booking request to **API Gateway** → **Booking Service**.
3. Booking Service:
   - Validates event details through **Event Service**.
   - Confirms and stores the booking.
   - **Publishes a booking event** to **RabbitMQ**.
4. **Consumers**:
   - **Dashboard Service** consumes booking event and updates organizer analytics.
   - **Notification Service** consumes booking event and calls **SendGrid** to send confirmation emails.

---

## 7. Ports Summary

- **User Service** – `8081`
- **Booking Service** – `8082`
- **Event Service** – `8083`
- **Search Service** – `8084`
- **Dashboard Service** – `8085`
- **RabbitMQ Broker** – `5672`

(Ports are logical assignments taken from the component interaction diagram.)

---

## 8. Key Design Choices

- **Microservice Architecture**  
  Each domain (user, event, search, booking, dashboard, notification) is isolated into its own service for scalability and independent deployment.

- **API Gateway**  
  Provides a single entry point for clients and hides internal service topology.

- **Event-Driven Communication**  
  RabbitMQ is used to decouple real-time booking from downstream processing (analytics, notifications).

- **Externalized Concerns**  
  Email delivery is delegated to **SendGrid**, a specialized email provider.

This document gives a consolidated view of the architecture depicted in the three diagrams, suitable for use as `architecture.md` in the repository.
