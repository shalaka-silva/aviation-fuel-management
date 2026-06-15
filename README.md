# AVFuel — Aviation Fuel Management System

Mobile-first aviation fuel management system. Helps airlines, charter operators, and aviation companies compare fuel options at airports and choose the best quotation based on price, fuel type, airport, vendor, currency, taxes, fees, and trip-planning needs.

## Prerequisites

- Java 21
- Docker and Docker Compose
- Maven (or use the included `./mvnw` wrapper)

## Local Development

### 1. Start PostgreSQL

```bash
docker compose up -d
```

### 2. Run the application

```bash
./mvnw spring-boot:run
```

The application starts on port **8080**.

### 3. Check health

```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{"status":"UP"}
```

### 4. Run tests

Tests use Testcontainers — Docker must be running. No manual database setup needed.

```bash
./mvnw clean test
```

### 5. Stop PostgreSQL

```bash
docker compose down
```

To also remove data volumes:

```bash
docker compose down -v
```

## Project Structure

```
src/main/java/com/fuelmanagement/
  FuelManagementApplication.java
  common/
    error/         Global exception handling
    money/         Money and precision value objects (future)
    validation/    Shared validation logic (future)
    audit/         Audit utilities (future)
  airport/         Airport master data module
  vendor/          Vendor master data module
  fuel/            Fuel type module
  quotation/       Fuel quotation and pricing module
  forex/           Currency and exchange rate module
  customer/        Customer and trip request module
```

Each module follows a layered structure:
- `domain/` — entities, value objects, domain rules
- `application/` — use cases and services
- `infrastructure/` — database repositories, external adapters
- `web/` — controllers and request/response DTOs

## Database Migrations

Migrations live in `src/main/resources/db/migration/` and follow Flyway naming:

```
V1__init_schema.sql
V2__create_airports_table.sql
```

**Never edit an already-applied migration.** Always create a new migration file.

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Build | Maven |
| Tests | JUnit 5, Testcontainers |

## Milestone Roadmap

- [x] Milestone 0: Skeleton project
- [ ] Milestone 1: Airport master data
- [ ] Milestone 2: Vendor and fuel type master data
- [ ] Milestone 3: Currency and forex foundation
- [ ] Milestone 4: Fuel quotation model
- [ ] Milestone 5: Precision calculation engine
- [ ] Milestone 6: Quote comparison
- [ ] Milestone 7: Trip-based recommendation
- [ ] Milestone 8: Auditability
- [ ] Milestone 9: Security
