# Product-Store-API

A microservices-based eCommerce-style backend service suite for product, order, payment, auth, etc.

Product-Store-API is a pet project implementing a microservices architecture inspired by large-scale systems. It demonstrates how to build several cooperating services that are decoupled, independently deployable, and scalable.

---

## Key features:

- Distributed services, each handling a single responsibility (auth, order, payment, notification, product etc.)

- Use of Spring Boot for service framework, Spring Security + JWT for authentication/authorization

- PostgreSQL as relational data store

- Kafka for inter-service messaging (order ↔ payment ↔ notification)

- A simple HTML + Mail-dev interface to simulate sending/receiving emails (notifications)

- Infrastructure as code: Dockerfiles per service; a docker-compose file to wire up services, networks, databases

---

## Microservices

The system is split into several independent services, each with a single responsibility:

- **auth-service** – handles user authentication, issues JWT tokens, and verifies identity for other services.

- **customer-service** – manages customer profiles, including update and delete operations after authentication.

- **product-service** – stores and manages product-related data such as categories, brands, and products.

- **order-service** – allows customers to create orders and coordinates with payment and notification services through Kafka.

- **payment-service** – processes payments when a customer places an order.

- **notification-service** – sends notification emails using Mail-dev for simulation.

- **server-config** – provides centralized configuration management for all services.

---

## Problem this solves

This project aims to demonstrate how a microservices architecture provides benefits such as:

- **Modularity:** each service focuses on a single domain; easier to maintain, test, extend

- **Decoupling & resilience:** one service can go down or be redeployed without bringing down the whole system

- **Scalability:** you can scale only the services under load

- **Independent deployment and development:** different teams or modules can evolve independently

- Even though it’s a pet project, many patterns here map to real-world systems (auth, messaging, microservices orchestration, etc.)

---

## Technology Stack

- Java / Spring Boot

- Spring Security + JWT

- PostgreSQL

- Kafka (as message broker)

- Docker & Docker Compose

- Mail-dev to simulate e-mail delivery

- HTML for email simulation

---

## Running Locally

1.  **Clone the repo:**
    
    ```bash
    git clone [https://github.com/milkevych/product-store-api.git](https://github.com/milkevych/product-store-api.git)
    cd product-store-api
    ```

2.  **Provide the needed configuration:**

### Generate RSA Keys for JWT

The `auth-service` uses RSA key pairs for signing and verifying JWT tokens. You must generate these keys before running the services.
From the root of the project, navigate to:
`product-store-api/services/auth-service/src/main/resources/keys/local-only`

Then run the following commands:
```bash
    # Generate a private key
    openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
    
    # Generate a public key from the private key
    openssl rsa -pubout -in private_key.pem -out public_key.pem
```
You should now have `private_key.pem` and `public_key.pem` inside the `keys/local-only` directory.

3.  **Configure JwtService in Other Services**

To allow other services (like `order-service` and `customer-service`) to validate JWT tokens, you need to update them with the same key-loading logic.

Example adjustment (in `JwtService`):
```java
    public JwtService() throws Exception {
        this.privateKey = KeyUtils.loadPrivateKey("keys/local-only/private_key.pem");
        this.publicKey = KeyUtils.loadPublicKey("keys/local-only/public_key.pem");
    }
```
`KeyUtils` is a helper class for loading keys. Copy the constructor logic above into any service that needs JWT validation.

4.  **Service Configuration (`application.yml`)**

Each microservice requires its own configuration, typically stored in `application.yml`. These include settings like:
- Database connections (PostgreSQL for each service)
- Kafka broker details
- Auth-service public key path (for JWT verification)

Currently, these YAML configs are stored in a private remote repository, so they are not included here.
You have two options:
1.  **Centralize configs in `server-config`** (recommended, as you already started doing).
    - Add your database, Kafka, and security configs there.
    - Each service will fetch its configuration from `server-config` at runtime.
2.  **Keep configs inside each service repo** (simpler for local dev, harder for consistency).
    - Add `application.yml` directly to each service under `src/main/resources`.
    - Manually configure DB, Kafka, and JWT paths.

For long-term maintainability, use option 1: keep everything in `server-config`. This way, you don’t need to duplicate configs across services, and it’s easier to run everything with Docker Compose.

5.  **Sample configuration for `order-service` (Kafka, DB)**

Each microservice (except `auth-service` and `server-config`) has its own PostgreSQL database.
- In **Docker Compose**, define separate containers or schemas for each service database.
- In **`application.yml`**, make sure each service’s config points to its own DB.

Example `order-service` for DB and Kafka:

```yaml
server:
  port: 8089
spring:
  # minimum setting for DB
  datasource:
    url: jdbc:postgresql://postgresql:5432/order
    username: store
    password: store
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create
    database: postgresql
  # minimum setting for Kafka producer.
  kafka:
    bootstrap-servers: kafka:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      properties:
        spring.json.add.type.headers: false
application:
  config:
    # Urls to other services for sending requests from order-service to other services using OpenFeing
    product-url: http://product-service:8090/api/v1/products
    customer-url: http://customer-service:8055/api/v1/customers 
```

6.  **Go to the project root and run it:**
```bash
    docker compose up --build
```
