@echo off
REM Purrfect Match Microservices Startup Script (Batch version)
REM This script starts all microservices in separate command windows

echo ========================================
echo Starting Purrfect Match Microservices
echo ========================================
echo.
echo Prerequisites Check:
echo - Ensure MariaDB is running on localhost:3306
echo - Ensure RabbitMQ is running on localhost:5672
echo.

REM Start services in separate windows
echo Starting Auth Service on port 8081...
start "Auth Service (8081)" cmd /k "cd auth-service && mvn spring-boot:run"

timeout /t 3 /nobreak >nul

echo Starting Pet Service on port 8082...
start "Pet Service (8082)" cmd /k "cd pet-service && mvn spring-boot:run"

timeout /t 3 /nobreak >nul

echo Starting Adoption Service on port 8083...
start "Adoption Service (8083)" cmd /k "cd adoption-service && mvn spring-boot:run"

timeout /t 3 /nobreak >nul

echo Starting Notification Service on port 8084...
start "Notification Service (8084)" cmd /k "cd notification-service && mvn spring-boot:run"

timeout /t 3 /nobreak >nul

echo Starting API Gateway on port 8080...
start "API Gateway (8080)" cmd /k "cd api-gateway && mvn spring-boot:run"

echo.
echo ========================================
echo All services are starting...
echo ========================================
echo.
echo Service URLs:
echo - API Gateway:     http://localhost:8080
echo - Auth Service:    http://localhost:8081
echo - Pet Service:     http://localhost:8082
echo - Adoption Service: http://localhost:8083
echo - Notification Service: http://localhost:8084
echo.
echo Wait for all services to finish starting (check each window)
pause

