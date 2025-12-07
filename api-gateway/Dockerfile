# Multi-stage build for API Gateway
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8090

# Set environment variables with defaults
ENV AUTH_SERVICE_URL=http://auth-service:8081
ENV PET_SERVICE_URL=http://pet-service:8082
ENV ADOPTION_SERVICE_URL=http://adoption-service:8083
ENV NOTIFICATION_SERVICE_URL=http://notification-service:8084
ENV SERVER_PORT=8090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


