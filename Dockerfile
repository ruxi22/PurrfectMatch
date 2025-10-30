# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file
COPY target/*.jar app.jar

# Expose port 8087
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
