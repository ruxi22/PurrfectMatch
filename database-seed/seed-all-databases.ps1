# PowerShell script to seed all databases at once
# Run this from the project root directory

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Seeding All Databases" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$mariadbPath = "mysql"
$username = "root"
$password = "root"
$host = "localhost"
$port = "3306"

# Check if mysql command is available
try {
    $null = Get-Command mysql -ErrorAction Stop
} catch {
    Write-Host "ERROR: mysql command not found!" -ForegroundColor Red
    Write-Host "Please add MariaDB/MySQL bin directory to your PATH" -ForegroundColor Yellow
    Write-Host "Or use MySQL Workbench/HeidiSQL to run the SQL files manually" -ForegroundColor Yellow
    exit 1
}

# Function to run SQL script
function Seed-Database {
    param(
        [string]$DatabaseName,
        [string]$ScriptPath
    )
    
    Write-Host "Seeding $DatabaseName..." -ForegroundColor Green
    
    $command = "$mariadbPath -u $username -p$password -h $host -P $port $DatabaseName < `"$ScriptPath`""
    
    try {
        Get-Content $ScriptPath | & $mariadbPath -u $username -p$password -h $host -P $port $DatabaseName
        Write-Host "  ✓ $DatabaseName seeded successfully" -ForegroundColor Green
        return $true
    } catch {
        Write-Host "  ✗ Failed to seed $DatabaseName" -ForegroundColor Red
        Write-Host "  Error: $_" -ForegroundColor Red
        return $false
    }
}

# Get the script directory
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir

# Seed all databases
$success = $true

$success = (Seed-Database -DatabaseName "AuthDB" -ScriptPath "$scriptDir\seed-auth-db.sql") -and $success
$success = (Seed-Database -DatabaseName "PetDB" -ScriptPath "$scriptDir\seed-pet-db.sql") -and $success
$success = (Seed-Database -DatabaseName "AdoptionDB" -ScriptPath "$scriptDir\seed-adoption-db.sql") -and $success
$success = (Seed-Database -DatabaseName "NotificationDB" -ScriptPath "$scriptDir\seed-notification-db.sql") -and $success

Write-Host ""
if ($success) {
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "All databases seeded successfully!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Test the APIs:" -ForegroundColor Yellow
    Write-Host "- Users: http://localhost:8085/api/auth/users" -ForegroundColor White
    Write-Host "- Pets: http://localhost:8085/api/pets" -ForegroundColor White
    Write-Host "- Adoptions: http://localhost:8085/api/adoptions" -ForegroundColor White
    Write-Host "- Notifications: http://localhost:8085/api/notifications" -ForegroundColor White
} else {
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "Some databases failed to seed!" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please check:" -ForegroundColor Yellow
    Write-Host "1. MariaDB is running" -ForegroundColor White
    Write-Host "2. Databases exist (run services once to create them)" -ForegroundColor White
    Write-Host "3. Credentials are correct (root/root)" -ForegroundColor White
}

