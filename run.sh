#!/bin/bash

cd srv

echo "Building all modules..."
mvn clean install -DskipTests

echo "Starting microservices..."

# Start services in background
mvn -pl user-service spring-boot:run &
USER_PID=$!

mvn -pl booking-service spring-boot:run &
SEARCH_PID=$!

mvn -pl event-service spring-boot:run &
EVENT_PID=$!

mvn -pl search spring-boot:run &
SEARCH_PID=$!

mvn -pl dashboard-service spring-boot:run &
SEARCH_PID=$!

# Give them a few seconds to start
sleep 10

# Finally start the aggregator
mvn -pl aggregator spring-boot:run &
AGG_PID=$!

echo "All services are running!"
echo "----------------------------------"
echo "User Service:        http://localhost:8081/swagger-ui.html"
echo "Booking Service:     http://localhost:8082/swagger-ui.html"
echo "Event Service:       http://localhost:8083/swagger-ui.html"
echo "Search Service:      http://localhost:8084/swagger-ui.html"
echo "Dashboard Service:   http://localhost:8085/swagger-ui.html"
echo "Aggregator:          http://localhost:8080/swagger-ui.html"
echo "----------------------------------"

# Wait for user input before stopping
read -p "Press [Enter] to stop all services..."

kill $USER_PID $EVENT_PID $SEARCH_PID $AGG_PID
echo "All services stopped."
