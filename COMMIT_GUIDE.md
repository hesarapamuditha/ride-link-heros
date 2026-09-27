# RideLink Git Commit & Branching Roadmap (10 Commits)

> **Important**: This guide is tailored to fulfill **LO4 / Rubric I3 (3 Marks)**:
> *"A sustained sequence of meaningful commits, branches, pull requests and reviews provides clear evidence of individual contribution. Changes are well described, appropriately scoped and integrated through the agreed workflow."*
>
> ⚠️ **Notice**: In accordance with your instruction, no commits or pushes have been executed automatically. Use the step-by-step commands below to execute your commits manually.

---

## 🚗 Part 1: Member 2 (Your Part) – Driver & Vehicle Service (10 Commits)

### Branch Strategy
Create and checkout your feature branch:
```bash
git checkout -b feature/driver-vehicle-service
```

---

### Step 1: Initialize Service Skeleton and Maven Dependencies
- **Scope**: Service structure, `pom.xml`, and MongoDB/Security configuration properties.
- **Commands**:
```bash
git add driver-vehicle-service/pom.xml driver-vehicle-service/src/main/resources/application.properties
git commit -m "feat(driver): initialize driver-vehicle-service skeleton with Spring Boot 3 and MongoDB configuration"
```

---

### Step 2: Define Domain Models and Enumerations
- **Scope**: Core MongoDB entity models (`Driver`, `Vehicle`, `Location`) and enums (`AvailabilityStatus`, `VerificationStatus`, `VehicleType`, `VehicleStatus`).
- **Commands**:
```bash
git add driver-vehicle-service/src/main/java/com/ridelink/driver/model/
git commit -m "feat(driver): add driver, vehicle, and location domain models with status enums"
```

---

### Step 3: Implement Spring Data MongoDB Repositories
- **Scope**: Data access layer for `DriverRepository` and `VehicleRepository` with custom query methods.
- **Commands**:
```bash
git add driver-vehicle-service/src/main/java/com/ridelink/driver/repository/
git commit -m "feat(driver): implement MongoDB repositories for drivers and vehicles with indexed lookups"
```

---

### Step 4: Create Validation & Data Transfer Objects (DTOs)
- **Scope**: Request and response DTOs with Jakarta Bean Validation (`@NotNull`, `@NotBlank`, `@DecimalMin`, `@DecimalMax`).
- **Commands**:
```bash
git add driver-vehicle-service/src/main/java/com/ridelink/driver/dto/
git commit -m "feat(driver): add validation request and response DTOs for driver and vehicle operations"
```

---

### Step 5: Implement Haversine Distance & ETA Calculation Utilities
- **Scope**: `DistanceCalculator` utility implementing great-circle distance algorithm for proximity matching.
- **Commands**:
```bash
git add driver-vehicle-service/src/main/java/com/ridelink/driver/util/
git commit -m "feat(driver): implement Haversine distance and estimated arrival calculation utility"
```

---

### Step 6: Implement Business Logic & Verification State Management
- **Scope**: `DriverServiceImpl` and `VehicleServiceImpl` enforcing rules (driver must be verified with active vehicle before going online).
- **Commands**:
```bash
git add driver-vehicle-service/src/main/java/com/ridelink/driver/service/
git commit -m "feat(driver): implement driver operational profile, vehicle assignment, and availability services"
```

---

### Step 7: Configure Global Exception Handling & Security / JWT
- **Scope**: `GlobalExceptionHandler`, RFC 7807 compliant error format, `JwtTokenProvider`, and `SecurityConfig`.
- **Commands**:
```bash
git add driver-vehicle-service/src/main/java/com/ridelink/driver/exception/ driver-vehicle-service/src/main/java/com/ridelink/driver/security/
git commit -m "feat(driver): add JWT authentication filter, security configuration, and global exception handling"
```

---

### Step 8: Build RESTful Controllers & OpenAPI / Swagger Documentation
- **Scope**: `DriverController`, `VehicleController`, and `OpenApiConfig`.
- **Commands**:
```bash
git add driver-vehicle-service/src/main/java/com/ridelink/driver/controller/ driver-vehicle-service/src/main/java/com/ridelink/driver/config/ driver-vehicle-service/src/main/java/com/ridelink/driver/DriverVehicleServiceApplication.java
git commit -m "feat(driver): expose REST endpoints for operational profile, location updates, and OpenAPI docs"
```

---

### Step 9: Add Comprehensive Unit Tests (Positive & Negative Cases)
- **Scope**: Unit tests covering driver registration, distance calculation, duplicate plate validation, and illegal state transitions.
- **Commands**:
```bash
git add driver-vehicle-service/src/test/
git commit -m "test(driver): add unit tests for driver service, vehicle service, and controller endpoints"
```

---

### Step 10: Interservice Driver Matching & Lifecycle Status Toggle
- **Scope**: Final integration endpoints for `/eligible` driver discovery and `/status` updates used by Ride Management Service.
- **Commands**:
```bash
git add driver-vehicle-service/
git commit -m "feat(driver): finalize interservice endpoint for eligible driver discovery and status coordination"
```

---

## 👥 Part 2: Commit Roadmap for Other 3 Microservices (Team Members)

### 👤 Member 1: Account Service (`account-service`)
Branch: `feature/account-service`
1. `feat(account): initialize account-service Maven project and application properties`
2. `feat(account): define User document model and Role/AccountStatus enums`
3. `feat(account): add UserRepository with email lookup and uniqueness indexing`
4. `feat(account): create user registration and authentication request/response DTOs`
5. `feat(account): configure BCrypt password encoder and security filter chain`
6. `feat(account): implement JWT token generation, parsing, and claims verification`
7. `feat(account): implement AccountService registration, login, and profile updating`
8. `feat(account): create AuthController and UserController with OpenAPI annotations`
9. `feat(account): add GlobalExceptionHandler for invalid credentials and duplicate users`
10. `test(account): add unit tests for authentication, password hashing, and user controller`

---

### 👤 Member 3: Ride Management Service (`ride-management-service`)
Branch: `feature/ride-service`
1. `feat(ride): initialize ride-management-service project and MongoDB config`
2. `feat(ride): define Ride document entity, RideStatus lifecycle enum, and LocationPoint`
3. `feat(ride): implement RideRepository with passenger and driver query methods`
4. `feat(ride): create RideRequest, RideResponse, and interservice DTO contracts`
5. `feat(ride): configure RestTemplate client for interservice REST communication`
6. `feat(ride): implement InterserviceClient to fetch available drivers and update driver status`
7. `feat(ride): implement RideService with strict lifecycle state machine transitions`
8. `feat(ride): implement ride completion workflow with automated payment triggering`
9. `feat(ride): expose RideController REST endpoints and configure OpenAPI documentation`
10. `test(ride): add unit tests for ride request, driver assignment, and negative state transitions`

---

### 👤 Member 4: Fare & Payment Service (`fare-payment-service`)
Branch: `feature/fare-payment-service`
1. `feat(payment): initialize fare-payment-service project and database configuration`
2. `feat(payment): define Payment entity, PaymentMethod, and PaymentStatus enums`
3. `feat(payment): create PaymentRepository for transaction lookup and receipt querying`
4. `feat(payment): create FareEstimateRequest and FareEstimateResponse DTOs`
5. `feat(payment): implement FareService with documented distance and vehicle multiplier rules`
6. `feat(payment): implement simulated PaymentService with receipt generation`
7. `feat(payment): add simulated card failure handling for negative scenario testing`
8. `feat(payment): expose FareController and PaymentController REST APIs with Swagger`
9. `feat(payment): configure GlobalExceptionHandler and security filters`
10. `test(payment): add unit tests for fare calculation formula, payment simulation, and receipts`

---

## 🔀 Merging into Main & CI Trigger
Once each feature branch is completed and reviewed via Pull Request:
```bash
git checkout -b main
# Merge feature branches or push root configuration:
git add pom.xml .gitignore .github/ postman/ README.md ARCHITECTURE.md COMMIT_GUIDE.md
git commit -m "chore: setup monorepo root pom, CI pipeline, and Postman test suite"
git push origin main
```
