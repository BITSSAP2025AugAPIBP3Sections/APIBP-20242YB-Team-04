#!/bin/bash

cd srv

echo "----------------------------------"
echo "Stopping any previously running services..."
echo "----------------------------------"

# Kill any leftover Spring Boot or Maven processes
pkill -f "spring-boot:run" 2>/dev/null || true
pkill -f "org.springframework.boot" 2>/dev/null || true
pkill -f "maven" 2>/dev/null || true

sleep 2
echo "Cleanup done. Starting fresh..."
echo "----------------------------------"

echo "Building all modules..."
mvn clean install -DskipTests

echo "Starting microservices..."

# Start each service in the background
mvn -pl user-service spring-boot:run &
USER_PID=$!

mvn -pl booking-service spring-boot:run &
BOOKING_PID=$!

mvn -pl event-service spring-boot:run &
EVENT_PID=$!

mvn -pl search spring-boot:run &
SEARCH_PID=$!

mvn -pl dashboard-service spring-boot:run &
DASHBOARD_PID=$!

# Give time to start
sleep 10

# Start the gateway
mvn -pl gateway spring-boot:run &
GATEWAY_PID=$!

echo "----------------------------------"
echo "All services are running!"
echo "----------------------------------"
echo "User Service:        http://localhost:8081/swagger-ui.html"
echo "Booking Service:     http://localhost:8082/swagger-ui.html"
echo "Event Service:       http://localhost:8083/swagger-ui.html"
echo "Search Service:      http://localhost:8084/swagger-ui.html"
echo "Dashboard Service:   http://localhost:8085/swagger-ui.html"
echo "Gateway:             http://localhost:8080/swagger-ui.html"
echo "----------------------------------"

# Wait for user input before stopping
read -p "Press [Enter] to stop all services..."

echo "Stopping all services..."

# Kill all tracked PIDs
kill -9 $USER_PID $BOOKING_PID $EVENT_PID $SEARCH_PID $DASHBOARD_PID $GATEWAY_PID 2>/dev/null || true

# Ensure no stray processes remain
pkill -f "spring-boot:run" 2>/dev/null || true
pkill -f "org.springframework.boot" 2>/dev/null || true
pkill -f "maven" 2>/dev/null || true

echo "All services stopped."
