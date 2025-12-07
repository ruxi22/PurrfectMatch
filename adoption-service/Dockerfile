# Multi-stage build for Adoption Service
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
EXPOSE 8083

# Set environment variables with defaults
ENV SPRING_DATASOURCE_URL=jdbc:mariadb://mariadb:3306/AdoptionDB
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=root
ENV SPRING_RABBITMQ_HOST=rabbitmq
ENV SPRING_RABBITMQ_PORT=5672
ENV SPRING_RABBITMQ_USERNAME=guest
ENV SPRING_RABBITMQ_PASSWORD=guest
ENV PET_SERVICE_URL=http://pet-service:8082
ENV AUTH_SERVICE_URL=http://auth-service:8081
ENV SERVER_PORT=8083

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


