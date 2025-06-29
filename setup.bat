@echo off

echo Setting up SafeRoute API Gateway...

echo Checking prerequisites...

java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java not found. Please install Java 17+
    pause
    exit /b 1
)
echo Java found

docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Docker not found. Please install Docker
    pause
    exit /b 1
)
echo Docker found

docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    docker compose version >nul 2>&1
    if %errorlevel% neq 0 (
        echo Docker Compose not found
        pause
        exit /b 1
    )
)
echo Docker Compose found

echo Building SafeRoute Gateway...
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)
echo Build successful!

echo Starting services...
docker-compose up -d

if %errorlevel% neq 0 (
    echo Failed to start services!
    pause
    exit /b 1
)

echo SafeRoute Gateway is now running!
echo Gateway: http://localhost:8080
echo Health: http://localhost:8080/actuator/health
echo Metrics: http://localhost:8080/actuator/prometheus

pause

REM Start services
echo.
echo 🚀 Starting services with Docker Compose...
docker-compose up -d

if %errorlevel% neq 0 (
    echo ❌ Failed to start services!
    pause
    exit /b 1
)

REM Wait for services to be ready
echo.
echo ⏳ Waiting for services to be ready...
timeout /t 30 /nobreak >nul

REM Display service URLs
echo.
echo 🎉 SafeRoute API Gateway is ready!
echo ==================================
echo 📡 Gateway URL:     http://localhost:8080
echo 📚 Swagger UI:      http://localhost:8080/swagger-ui.html
echo 💓 Health Check:    http://localhost:8080/api/health
echo 📊 Prometheus:      http://localhost:9090
echo 📈 Grafana:         http://localhost:3000 (admin/admin123)
echo.

REM Demo commands
echo 🚀 Quick Start Commands:
echo ==================================
echo # Get JWT Token (using PowerShell):
echo Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -ContentType "application/json" -Body '{"username":"admin","password":"admin123"}'
echo.
echo # Or using curl if available:
echo curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
echo.
echo # View logs:
echo docker-compose logs -f api-gateway
echo.
echo # Stop services:
echo docker-compose down

echo.
echo 📖 For more information, check the README.md file
echo 🎯 Happy coding with SafeRoute! 🛡️
pause
