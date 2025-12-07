# Purrfect Match Microservices Startup Script
# This script starts all microservices in separate windows

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting Purrfect Match Microservices" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if MariaDB and RabbitMQ are running
Write-Host "Prerequisites Check:" -ForegroundColor Yellow
Write-Host "- Ensure MariaDB is running on localhost:3306" -ForegroundColor Yellow
Write-Host "- Ensure RabbitMQ is running on localhost:5672" -ForegroundColor Yellow
Write-Host ""

# Function to start a service in a new window
function Start-Service {
    param(
        [string]$ServiceName,
        [string]$ServicePath,
        [string]$Port
    )
    
    Write-Host "Starting $ServiceName on port $Port..." -ForegroundColor Green
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$ServicePath'; Write-Host 'Starting $ServiceName on port $Port...' -ForegroundColor Cyan; mvn spring-boot:run"
    Start-Sleep -Seconds 3
}

# Start services in order (API Gateway last, as it depends on others)
Write-Host "Starting services..." -ForegroundColor Yellow
Write-Host ""

Start-Service -ServiceName "Auth Service" -ServicePath "$PSScriptRoot\auth-service" -Port "8081"
Start-Service -ServiceName "Pet Service" -ServicePath "$PSScriptRoot\pet-service" -Port "8082"
Start-Service -ServiceName "Adoption Service" -ServicePath "$PSScriptRoot\adoption-service" -Port "8083"
Start-Service -ServiceName "Notification Service" -ServicePath "$PSScriptRoot\notification-service" -Port "8084"
Start-Service -ServiceName "API Gateway" -ServicePath "$PSScriptRoot\api-gateway" -Port "8085"

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "All services are starting..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Service URLs:" -ForegroundColor Yellow
Write-Host "- API Gateway:     http://localhost:8085" -ForegroundColor White
Write-Host "- Auth Service:    http://localhost:8081" -ForegroundColor White
Write-Host "- Pet Service:     http://localhost:8082" -ForegroundColor White
Write-Host "- Adoption Service: http://localhost:8083" -ForegroundColor White
Write-Host "- Notification Service: http://localhost:8084" -ForegroundColor White
Write-Host ""
Write-Host "Wait for all services to finish starting (check each window)" -ForegroundColor Yellow
Write-Host "Press any key to exit this window (services will continue running)..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

