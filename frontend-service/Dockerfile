# Multi-stage build for Frontend Service
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
EXPOSE 8085

# Set environment variables with defaults
ENV API_GATEWAY_URL=http://api-gateway:8080
ENV SERVER_PORT=8085

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


