# Ticket Booking System

A high-concurrency ticket booking system designed to handle hundreds of thousands of requests per second while preventing double-booking through distributed locking mechanisms.

## Architecture Overview

This system uses a **microservices architecture** with the following components:

| Service | Port | Description |
|---------|------|-------------|
| inventory-service | 8081 | Manages events and seat availability |
| seat-lock-service | 8082 | Handles distributed seat locking with Redis |
| booking-service | 8083 | Core booking orchestration |
| payment-service | 8084 | Payment processing integration |
| notification-service | 8085 | Email/SMS notifications |
| websocket-service | 8086 | Real-time seat status updates |

## Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.4.x
- **Build Tool**: Gradle 8.x
- **Database**: MySQL 8.0
- **Cache/Locking**: Redis 7
- **Message Queue**: Apache Kafka
- **Migration**: Flyway
- **Containerization**: Docker & Docker Compose

## Project Structure

```
ticket-booking-system/
├── build.gradle                 # Parent build configuration
├── settings.gradle              # Module configuration
├── gradle.properties            # Shared properties
├── shared/
│   └── common/                  # Shared DTOs, utils, exceptions
├── services/
│   ├── inventory-service/       # Event & seat management
│   ├── booking-service/         # Booking orchestration (MAIN)
│   ├── seat-lock-service/       # Distributed locking
│   ├── payment-service/         # Payment processing
│   ├── notification-service/    # Notifications
│   └── websocket-service/       # Real-time updates
├── infrastructure/
│   ├── docker-compose.yml       # Infrastructure containers
│   ├── docker-compose-services.yml  # Service containers
│   ├── docker/                  # Docker configs
│   └── scripts/                 # Build & deploy scripts
└── docs/                        # Documentation
```

## Quick Start

### Prerequisites

- Java 21
- Docker & Docker Compose
- Gradle 8.x (or use wrapper)

### 1. Start Infrastructure

```bash
cd infrastructure
./scripts/deploy.sh up --infra
```

This starts:
- MySQL (port 3306)
- Redis (port 6379)
- Kafka (port 9092)
- Kafka UI (port 8090)
- Prometheus (port 9090)
- Grafana (port 3000)

### 2. Build Services

```bash
./infrastructure/scripts/build.sh --all
```

### 3. Start Services

```bash
./infrastructure/scripts/deploy.sh up --services
```

### 4. Verify

- Booking Service Swagger: http://localhost:8083/swagger-ui.html
- Kafka UI: http://localhost:8090
- Grafana: http://localhost:3000 (admin/admin)

## API Endpoints (Booking Service)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/bookings` | Create new booking |
| GET | `/api/v1/bookings/{id}` | Get booking by ID |
| PUT | `/api/v1/bookings/{id}/confirm` | Confirm booking |
| PUT | `/api/v1/bookings/{id}/cancel` | Cancel booking |
| GET | `/api/v1/bookings/users/{userId}` | Get user's bookings |

## Concurrency Handling

The system uses a **two-layer locking strategy**:

1. **Distributed Locks (Redis)**: Fast, atomic seat locking using SETNX with TTL
2. **Optimistic Locking (Database)**: Version-based conflict detection on commit

### Booking Flow

1. User selects seats → Lock acquired via Redis (10 min TTL)
2. User proceeds to payment → Lock extended if needed
3. Payment success → Booking confirmed, seats marked as BOOKED
4. Payment timeout → Lock expires, seats released automatically

## Development

### Build Single Service

```bash
./gradlew :services:booking-service:build
```

### Run Tests

```bash
./gradlew :services:booking-service:test
```

### Run Locally

```bash
./gradlew :services:booking-service:bootRun
```

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| DB_HOST | localhost | MySQL host |
| DB_PORT | 3306 | MySQL port |
| DB_NAME | booking_db | Database name |
| REDIS_HOST | localhost | Redis host |
| KAFKA_BOOTSTRAP_SERVERS | localhost:9092 | Kafka servers |

## License

MIT License
