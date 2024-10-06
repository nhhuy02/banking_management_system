# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the build artifact (JAR file) from the host to the container
COPY build/libs/banking-report-service-0.0.1-SNAPSHOT.jar /app/banking-report-service.jar

# Expose the application's port (8086)
EXPOSE 8086

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/banking-report-service.jar"]
