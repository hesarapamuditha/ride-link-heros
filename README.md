# RideLink - Backend Microservices for a Ride-Sharing Platform

[![CI Pipeline](https://github.com/hesarapamuditha/ride-link-heros/actions/workflows/ci.yml/badge.svg)](https://github.com/hesarapamuditha/ride-link-heros/actions/workflows/ci.yml)
**Module**: IT3130 - Application Development  
**Assignment**: Group Assignment (Backend Microservices Solution)  
**Platform**: Java 17+, Spring Boot 3.3.4, Spring Data MongoDB, SpringDoc OpenAPI (Swagger 3.0), JUnit 5

---

## 👥 Service Ownership & Group Allocation

| # | Microservice | Primary Owner | Port | Persistence Boundary |
|---|---|---|---|---|
| 1 | **Account Service** | Member 1 | `8081` | `ridelink_account_db` |
| 2 | **Driver & Vehicle Service** | **Member 2 (My Part)** | `8082` | `ridelink_driver_db` |
| 3 | **Ride Management Service** | Member 3 | `8083` | `ridelink_ride_db` |
| 4 | **Fare & Payment Service** | Member 4 | `8084` | `ridelink_fare_db` |

> 📦 **Team Deliverables**: Standalone ZIP packages for the other 3 microservices are available in the root repository directory:
> - `account-service.zip`
> - `ride-management-service.zip`
> - `fare-payment-service.zip`

---

## 🛠️ Prerequisites

- **Java JDK**: Version 17 or higher (Java 17, 21, and 26 supported)
- **Apache Maven**: Version 3.8+
- **MongoDB**: Either a local MongoDB instance running on `localhost:27017` OR a free Cloud MongoDB Atlas Cluster.
- **Postman**: For executing the end-to-end test suite (`postman/RideLink_API.postman_collection.json`).

---

## ⚙️ Configuration & Cloud MongoDB Setup

Each microservice is preconfigured with sensible local defaults and seamlessly supports **Cloud MongoDB Atlas** via the `SPRING_DATA_MONGODB_URI` environment variable.

### Setting Cloud MongoDB Atlas URI:
To connect to your MongoDB Atlas cluster, export the connection string before starting the services:

```bash
# Example Cloud MongoDB Atlas Connection String
export SPRING_DATA_MONGODB_URI="mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority"
```

Each microservice automatically appends its dedicated database name:
- `ridelink_account_db` (Account Service)
- `ridelink_driver_db` (Driver & Vehicle Service)
- `ridelink_ride_db` (Ride Management Service)
- `ridelink_fare_db` (Fare & Payment Service)

### Port Configuration:
- `Account Service`: `http://localhost:8081`
- `Driver & Vehicle Service`: `http://localhost:8082`
- `Ride Management Service`: `http://localhost:8083`
- `Fare & Payment Service`: `http://localhost:8084`

---

## 🚀 Recommended Start-up Sequence

To run the complete integrated solution, launch the services in separate terminal windows in the following order:

### 1. Start Account Service (Port 8081)
```bash
cd account-service
mvn spring-boot:run
```

### 2. Start Driver & Vehicle Service (Port 8082) – *My Service*
```bash
cd driver-vehicle-service
mvn spring-boot:run
```

### 3. Start Fare & Payment Service (Port 8084)
```bash
cd fare-payment-service
mvn spring-boot:run
```

### 4. Start Ride Management Service (Port 8083)
```bash
cd ride-management-service
mvn spring-boot:run
```

---

## 📖 Swagger UI / OpenAPI Documentation

Each microservice features self-documenting interactive Swagger UI consoles:

| Microservice | Interactive Swagger UI URL | Raw OpenAPI 3.0 JSON |
|---|---|---|
| **Account Service** | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) | [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs) |
| **Driver & Vehicle Service** | [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) | [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs) |
| **Ride Management Service** | [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) | [http://localhost:8083/v3/api-docs](http://localhost:8083/v3/api-docs) |
| **Fare & Payment Service** | [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html) | [http://localhost:8084/v3/api-docs](http://localhost:8084/v3/api-docs) |

---

## 🧪 Testing Instructions & Evidence

### Running Automated Unit Tests
To execute all 34+ unit tests across all four microservices from the project root:

```bash
mvn clean test
```

To run unit tests for an individual microservice:
```bash
# Driver & Vehicle Service (My Service)
cd driver-vehicle-service && mvn test

# Account Service
cd account-service && mvn test

# Ride Management Service
cd ride-management-service && mvn test

# Fare & Payment Service
cd fare-payment-service && mvn test
```

### Running the Postman Test Suite
1. Launch Postman.
2. Import `postman/RideLink_API.postman_collection.json`.
3. Import `postman/RideLink_Environment.postman_environment.json`.
4. Select the **RideLink Local Microservices Environment**.
5. Execute requests sequentially from Folder `01` through Folder `04`.

---

## 🔒 Sample Credentials & Test Data

### 1. Sample Passenger Account
- **Email**: `passenger@ridelink.com`
- **Password**: `pass1234`
- **Role**: `ROLE_PASSENGER`

### 2. Sample Driver Account
- **Email**: `driver@ridelink.com`
- **Password**: `drive1234`
- **Role**: `ROLE_DRIVER`
- **License**: `B-8839201`
- **Vehicle Plate**: `WP-CAD-7890` (Toyota Prius)
- **Simulated GPS Location**: `6.9344, 79.8428` (Fort Railway Station, Colombo)

### 3. Negative Scenario Triggers (For Demonstration / Viva)
1. **Invalid Driver Availability Transition**: Trying to set availability to `AVAILABLE` without verified profile or registered vehicle (`400 Bad Request`).
2. **Duplicate Vehicle Plate**: Attempting to register `WP-CAD-7890` a second time (`409 Conflict`).
3. **Invalid GPS Bounds**: Sending latitude `999.0` (`400 Bad Request`).
4. **Invalid Ride State Transition**: Trying to complete a ride before starting it (`400 Bad Request`).
5. **No Available Driver**: Requesting a driver in an area with no available vehicles (`404 Not Found`).
6. **Simulated Card Failure**: Submitting a credit card ending in `0000` to simulate insufficient funds (`402 Payment Required`).

---

## 📌 Git Workflow & Commit Guidelines
See [COMMIT_GUIDE.md](file:///Users/hesarapamuditha/Documents/ride-link-heros/COMMIT_GUIDE.md) for the exact 10-step commit roadmap for Member 2 (Driver & Vehicle Service) and all team members.
