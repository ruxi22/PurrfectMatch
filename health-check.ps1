
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Purrfect Match - Health Check" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$allServicesOk = $true

function Test-Service {
    param(
        [string]$ServiceName,
        [string]$Url,
        [int]$Port
    )
    
    Write-Host "Checking $ServiceName (Port $Port)..." -ForegroundColor Yellow -NoNewline
    
    # Check if port is listening
    $portCheck = netstat -ano | findstr ":$Port" | findstr "LISTENING"
    
    if ($portCheck) {
        try {
            $response = Invoke-WebRequest -Uri $Url -TimeoutSec 3 -ErrorAction Stop
            Write-Host "  OK" -ForegroundColor Green
            return $true
        } catch {
            Write-Host "  Port open but service may have issues" -ForegroundColor Yellow
            return $false
        }
    } else {
        Write-Host " NOT RUNNING" -ForegroundColor Red
        return $false
    }
}

# Check API Gateway
Write-Host "`n1. API Gateway" -ForegroundColor Cyan
$gatewayOk = Test-Service -ServiceName "API Gateway" -Url "http://localhost:8085/api/pets" -Port 8085
$allServicesOk = $allServicesOk -and $gatewayOk

# Check Auth Service
Write-Host "`n2. Auth Service" -ForegroundColor Cyan
$authOk = Test-Service -ServiceName "Auth Service" -Url "http://localhost:8081/api/auth/users" -Port 8081
$allServicesOk = $allServicesOk -and $authOk

# Check Pet Service
Write-Host "`n3. Pet Service" -ForegroundColor Cyan
$petOk = Test-Service -ServiceName "Pet Service" -Url "http://localhost:8082/api/pets" -Port 8082
$allServicesOk = $allServicesOk -and $petOk

# Check Adoption Service
Write-Host "`n4. Adoption Service" -ForegroundColor Cyan
$adoptionOk = Test-Service -ServiceName "Adoption Service" -Url "http://localhost:8083/api/adoptions" -Port 8083
$allServicesOk = $allServicesOk -and $adoptionOk

# Check Notification Service
Write-Host "`n5. Notification Service" -ForegroundColor Cyan
$notificationOk = Test-Service -ServiceName "Notification Service" -Url "http://localhost:8084/api/notifications" -Port 8084
$allServicesOk = $allServicesOk -and $notificationOk

# Check MariaDB
Write-Host "`n6. MariaDB" -ForegroundColor Cyan
$mariadbCheck = netstat -ano | findstr ":3306" | findstr "LISTENING"
if ($mariadbCheck) {
    Write-Host " ✅ MariaDB is running on port 3306" -ForegroundColor Green
} else {
    Write-Host " ❌ MariaDB is NOT running on port 3306" -ForegroundColor Red
    $allServicesOk = $false
}

# Check RabbitMQ
Write-Host "`n7. RabbitMQ" -ForegroundColor Cyan
$rabbitmqCheck = netstat -ano | findstr ":5672" | findstr "LISTENING"
if ($rabbitmqCheck) {
    Write-Host " RabbitMQ is running on port 5672" -ForegroundColor Green
} else {
    Write-Host " RabbitMQ is NOT running on port 5672" -ForegroundColor Red
    $allServicesOk = $false
}

# Summary
Write-Host "`n========================================" -ForegroundColor Cyan
if ($allServicesOk) {
    Write-Host "ALL SERVICES ARE RUNNING!" -ForegroundColor Green
    Write-Host "`nYour application is ready to use!" -ForegroundColor Green
    Write-Host "`nNext steps:" -ForegroundColor Yellow
    Write-Host "1. Seed databases: .\database-seed\seed-all-databases.bat" -ForegroundColor White
    Write-Host "2. Open frontend: cd frontend && python -m http.server 3000" -ForegroundColor White
    Write-Host "3. Test the website at http://localhost:3000" -ForegroundColor White
} else {
    Write-Host " SOME SERVICES ARE NOT RUNNING" -ForegroundColor Red
    Write-Host "`nPlease start the missing services:" -ForegroundColor Yellow
    Write-Host "Run: .\start-all-services.ps1" -ForegroundColor White
}
Write-Host "========================================" -ForegroundColor Cyan

