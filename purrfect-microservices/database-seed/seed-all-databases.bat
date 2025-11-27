@echo off
REM Batch script to seed all databases at once
REM Run this from the project root directory

echo ========================================
echo Seeding All Databases
echo ========================================
echo.

set MYSQL_USER=root
set MYSQL_PASS=root
set MYSQL_HOST=localhost
set MYSQL_PORT=3306

REM Get the script directory
set SCRIPT_DIR=%~dp0

echo Seeding AuthDB...
mysql -u %MYSQL_USER% -p%MYSQL_PASS% -h %MYSQL_HOST% -P %MYSQL_PORT% AuthDB < "%SCRIPT_DIR%seed-auth-db.sql"
if %ERRORLEVEL% EQU 0 (
    echo   [OK] AuthDB seeded successfully
) else (
    echo   [ERROR] Failed to seed AuthDB
)

echo.
echo Seeding PetDB...
mysql -u %MYSQL_USER% -p%MYSQL_PASS% -h %MYSQL_HOST% -P %MYSQL_PORT% PetDB < "%SCRIPT_DIR%seed-pet-db.sql"
if %ERRORLEVEL% EQU 0 (
    echo   [OK] PetDB seeded successfully
) else (
    echo   [ERROR] Failed to seed PetDB
)

echo.
echo Seeding AdoptionDB...
mysql -u %MYSQL_USER% -p%MYSQL_PASS% -h %MYSQL_HOST% -P %MYSQL_PORT% AdoptionDB < "%SCRIPT_DIR%seed-adoption-db.sql"
if %ERRORLEVEL% EQU 0 (
    echo   [OK] AdoptionDB seeded successfully
) else (
    echo   [ERROR] Failed to seed AdoptionDB
)

echo.
echo Seeding NotificationDB...
mysql -u %MYSQL_USER% -p%MYSQL_PASS% -h %MYSQL_HOST% -P %MYSQL_PORT% NotificationDB < "%SCRIPT_DIR%seed-notification-db.sql"
if %ERRORLEVEL% EQU 0 (
    echo   [OK] NotificationDB seeded successfully
) else (
    echo   [ERROR] Failed to seed NotificationDB
)

echo.
echo ========================================
echo Seeding Complete!
echo ========================================
echo.
echo Test the APIs:
echo - Users: http://localhost:8085/api/auth/users
echo - Pets: http://localhost:8085/api/pets
echo - Adoptions: http://localhost:8085/api/adoptions
echo - Notifications: http://localhost:8085/api/notifications
echo.
pause

