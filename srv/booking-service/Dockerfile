# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy

# Set workdir
WORKDIR /app

# Add the jar (will be created by mvn package)
ARG JAR_FILE=target/booking-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","/app/app.jar"]
