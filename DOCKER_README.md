# Docker Setup Guide - Purrfect Match Microservices

This guide provides clear, step-by-step instructions for running the Purrfect Match microservices application using Docker and Docker Compose.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Architecture Overview](#architecture-overview)
- [Service Details](#service-details)
- [Common Commands](#common-commands)
- [Accessing Services](#accessing-services)
- [Environment Variables](#environment-variables)

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **Docker Desktop** (Windows/Mac) or **Docker Engine** (Linux)
  - Download: https://www.docker.com/products/docker-desktop
  - Minimum version: Docker 20.10+
- **Docker Compose**
  - Usually included with Docker Desktop
  - Minimum version: Docker Compose 2.0+

### Verify Installation

```bash
# Check Docker version
docker --version

# Check Docker Compose version
docker-compose --version

# Verify Docker is running
docker ps
```

---

## Quick Start

### 1. Start All Services

From the project root directory, run:

```bash
docker-compose up -d
```

This single command will:
- Build all 7 microservices (if not already built)
- Start MariaDB database
- Start RabbitMQ message broker
- Start all microservices in the correct order
- Create necessary networks and volumes
- Set up health checks and dependencies

### 2. Wait for Services to Initialize

Wait **1-2 minutes** for all services to start. You can monitor the startup process:

```bash
# Watch all logs
docker-compose logs -f

# Or check service status
docker-compose ps
```

### 3. Verify Services are Running

```bash
# Check all containers are healthy
docker-compose ps

# Test API Gateway
curl http://localhost:8090/auth/users

# Or open in browser
# Frontend: http://localhost:8085
# API Gateway: http://localhost:8090
```

Your entire microservices application is now running.

---

## Architecture Overview

The Docker setup includes **7 services** organized as follows:

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend Service                      │
│                    (Port 8085)                           │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                    API Gateway                           │
│                    (Port 8090)                          │
└───┬────────┬──────────┬──────────┬──────────────────────┘
    │        │          │          │
    ▼        ▼          ▼          ▼
┌──────┐ ┌──────┐ ┌──────────┐ ┌──────────────┐
│ Auth │ │ Pet  │ │ Adoption  │ │ Notification │
│ 8081 │ │ 8082 │ │   8083   │ │    8084      │
└──┬───┘ └──┬───┘ └────┬─────┘ └──────┬───────┘
   │        │          │              │
   └────────┴──────────┴──────────────┘
                       │              
                  ┌────▼─────┐  
                  │ MariaDB  │   
                  │  3306    │   
                  └──────────┘   
```

### Service Dependencies

- **MariaDB** → All microservices depend on it
- **Auth Service** → Must start before Adoption Service
- **Pet Service** → Must start before Adoption Service
- **API Gateway** → Depends on all backend services
- **Frontend Service** → Depends on API Gateway

Docker Compose automatically handles these dependencies using `depends_on` and health checks.

---

## Service Details

### Infrastructure Services

#### MariaDB (Database)
- **Container**: `purrfect-mariadb`
- **Port**: `3306`
- **Databases**: 
  - `AuthDB` (Auth Service)
  - `PetDB` (Pet Service)
  - `AdoptionDB` (Adoption Service)
  - `NotificationDB` (Notification Service)
- **Credentials**: 
  - Username: `root`
  - Password: `root`
- **Data Persistence**: Volume `mariadb_data`
- **Initialization**: SQL scripts in `./database-seed/` run automatically



### Microservices

#### 1. Auth Service
- **Container**: `purrfect-auth-service`
- **Port**: `8081`
- **Database**: `AuthDB`
- **Purpose**: User authentication, registration, JWT token generation
- **Health Check**: `http://localhost:8081/actuator/health`

#### 2. Pet Service
- **Container**: `purrfect-pet-service`
- **Port**: `8082`
- **Database**: `PetDB`
- **Purpose**: Pet catalog management, photo storage
- **Volume**: `pet_uploads` (for uploaded pet photos)
- **Health Check**: `http://localhost:8082/actuator/health`

#### 3. Adoption Service
- **Container**: `purrfect-adoption-service`
- **Port**: `8083`
- **Database**: `AdoptionDB`
- **Purpose**: Adoption request workflow management
- **Dependencies**: Auth Service, Pet Service, RabbitMQ
- **Health Check**: `http://localhost:8083/actuator/health`

#### 4. Notification Service
- **Container**: `purrfect-notification-service`
- **Port**: `8084`
- **Database**: `NotificationDB`
- **Purpose**: Asynchronous notification handling via RabbitMQ
- **Dependencies**: RabbitMQ
- **Health Check**: `http://localhost:8084/actuator/health`

#### 5. API Gateway
- **Container**: `purrfect-api-gateway`
- **Port**: `8090`
- **Purpose**: Centralized routing, CORS handling, request forwarding
- **Routes**:
  - `/auth/**` → Auth Service
  - `/api/pets/**` → Pet Service
  - `/api/adoptions/**` → Adoption Service
  - `/api/notifications/**` → Notification Service
- **Health Check**: `http://localhost:8090/actuator/health`

#### 6. Frontend Service
- **Container**: `purrfect-frontend-service`
- **Port**: `8085`
- **Purpose**: Serves HTML/CSS/JavaScript frontend
- **Dependencies**: API Gateway
- **Access**: `http://localhost:8085`

---

## Common Commands

### Starting Services

```bash
# Start all services in detached mode (background)
docker-compose up -d

# Start and rebuild all images
docker-compose up -d --build

# Start specific service
docker-compose up -d auth-service

# Start with logs visible
docker-compose up
```

### Stopping Services

```bash
# Stop all services (containers remain)
docker-compose stop

# Stop and remove containers
docker-compose down

# Stop and remove containers + volumes (⚠️ deletes all data)
docker-compose down -v
```

### Viewing Logs

```bash
# View all logs
docker-compose logs -f

# View logs for specific service
docker-compose logs -f auth-service

# View last 100 lines
docker-compose logs --tail=100 api-gateway

# View logs without following
docker-compose logs
```

### Service Status

```bash
# Check status of all services
docker-compose ps

# Check if specific service is running
docker-compose ps auth-service

# View resource usage
docker stats
```

### Rebuilding Services

```bash
# Rebuild specific service
docker-compose build auth-service

# Rebuild all services
docker-compose build

# Rebuild and restart
docker-compose up -d --build
```

### Database Operations

```bash
# Connect to MariaDB
docker-compose exec mariadb mariadb -uroot -proot

# Execute SQL file
docker-compose exec -T mariadb mariadb -uroot -proot AuthDB < database-seed/seed-auth-db.sql

# Backup database
docker-compose exec mariadb mysqldump -uroot -proot AuthDB > backup.sql

# View database logs
docker-compose logs mariadb
```

### Container Management

```bash
# Execute command in running container
docker-compose exec auth-service sh

# Restart specific service
docker-compose restart auth-service

# View container details
docker inspect purrfect-auth-service

# Remove stopped containers
docker-compose rm
```

---

## Accessing Services

### Web Interfaces

| Service | URL | Credentials |
|---------|-----|-------------|
| **Frontend** | http://localhost:8085 | N/A |
| **API Gateway** | http://localhost:8090 | N/A |

### API Endpoints (via API Gateway)

All API requests should go through the API Gateway at `http://localhost:8090`:

```bash
# Auth Service
POST http://localhost:8090/auth/register
POST http://localhost:8090/auth/login
GET  http://localhost:8090/auth/users

# Pet Service
GET  http://localhost:8090/api/pets
POST http://localhost:8090/api/pets
GET  http://localhost:8090/api/pets/{id}

# Adoption Service
GET  http://localhost:8090/api/adoptions
POST http://localhost:8090/api/adoptions
PUT  http://localhost:8090/api/adoptions/{id}/status

# Notification Service
GET  http://localhost:8090/api/notifications?userId=1
PUT  http://localhost:8090/api/notifications/{id}/read
```

### Direct Service Access (for debugging)

You can also access services directly (bypassing the gateway):

- Auth Service: http://localhost:8081
- Pet Service: http://localhost:8082
- Adoption Service: http://localhost:8083
- Notification Service: http://localhost:8084


---

## Environment Variables

All services can be configured using environment variables in `docker-compose.yml`. Key variables:

### Database Configuration

```yaml
SPRING_DATASOURCE_URL: jdbc:mariadb://mariadb:3306/AuthDB
SPRING_DATASOURCE_USERNAME: root
SPRING_DATASOURCE_PASSWORD: root
```

### Service URLs (for inter-service communication)

```yaml
AUTH_SERVICE_URL: http://auth-service:8081
PET_SERVICE_URL: http://pet-service:8082
ADOPTION_SERVICE_URL: http://adoption-service:8083
NOTIFICATION_SERVICE_URL: http://notification-service:8084
```

### Server Ports

```yaml
SERVER_PORT: 8081  # Auth Service
SERVER_PORT: 8082  # Pet Service
SERVER_PORT: 8083  # Adoption Service
SERVER_PORT: 8084  # Notification Service
SERVER_PORT: 8090  # API Gateway
SERVER_PORT: 8085  # Frontend Service
```

**Note**: To change these values, edit `docker-compose.yml` and restart services.




