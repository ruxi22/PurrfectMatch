
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Creating Users via API" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$apiGateway = "http://localhost:8085"

# Function to create user
function Create-User {
    param(
        [string]$Username,
        [string]$Password
    )
    
    Write-Host "Creating user: $Username..." -ForegroundColor Green
    
    $body = @{
        username = $Username
        password = $Password
    } | ConvertTo-Json
    
    try {
        $response = Invoke-RestMethod -Uri "$apiGateway/api/auth/register" `
            -Method Post `
            -ContentType "application/json" `
            -Body $body
        
        Write-Host "  ✓ User $Username created successfully" -ForegroundColor Green
        Write-Host "    Token: $($response.token.Substring(0, 20))..." -ForegroundColor Gray
        return $true
    } catch {
        if ($_.Exception.Response.StatusCode -eq 400) {
            Write-Host "  ⚠ User $Username already exists" -ForegroundColor Yellow
            return $true
        } else {
            Write-Host "  ✗ Failed to create user $Username" -ForegroundColor Red
            Write-Host "    Error: $($_.Exception.Message)" -ForegroundColor Red
            return $false
        }
    }
}

# Check if API Gateway is running
Write-Host "Checking API Gateway..." -ForegroundColor Yellow
try {
    $null = Invoke-WebRequest -Uri "$apiGateway/api/auth/users" -Method Get -TimeoutSec 2
    Write-Host "  ✓ API Gateway is running" -ForegroundColor Green
} catch {
    Write-Host "  ✗ API Gateway is not running!" -ForegroundColor Red
    Write-Host "    Please start all services first" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "Creating users..." -ForegroundColor Yellow
Write-Host ""

# Create regular users (ADOPTER role - default)
$success = $true
$success = (Create-User -Username "john_doe" -Password "password123") -and $success
$success = (Create-User -Username "jane_smith" -Password "password123") -and $success
$success = (Create-User -Username "sarah_wilson" -Password "password123") -and $success
$success = (Create-User -Username "mike_jones" -Password "password123") -and $success

Write-Host ""
Write-Host "Note: Admin user needs to be created manually or via SQL" -ForegroundColor Yellow
Write-Host "      because the API defaults to ADOPTER role" -ForegroundColor Yellow

Write-Host ""
if ($success) {
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "Users created successfully!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Test login:" -ForegroundColor Yellow
    Write-Host "  POST http://localhost:8085/api/auth/login" -ForegroundColor White
    Write-Host "  Body: {`"username`":`"john_doe`",`"password`":`"password123`"}" -ForegroundColor White
} else {
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "Some users failed to create!" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
}

