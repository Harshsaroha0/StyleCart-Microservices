# StyleCart — E-Commerce Microservices Backend

StyleCart is a learning-focused e-commerce backend built using **Java, Spring Boot, and Spring Cloud Microservices**.

The project demonstrates how an e-commerce backend can be divided into independent services for authentication, users, catalog, inventory, cart, orders, and payments.

The main focus of the project is understanding **microservices architecture, service discovery, centralized configuration, API Gateway, JWT authentication, Spring Security, REST APIs, database interaction, and inter-service communication**.

---

## 🏗️ Architecture

```text
                              CLIENT
                                │
                                ▼
                       ┌─────────────────┐
                       │   API GATEWAY   │
                       │      :8080      │
                       └────────┬────────┘
                                │
          ┌─────────────────────┼─────────────────────┐
          │                     │                     │
          ▼                     ▼                     ▼
   ┌─────────────┐       ┌─────────────┐       ┌─────────────┐
   │ Auth Service│       │ User Service│       │   Catalog   │
   │    :8087    │       │    :8085    │       │    :8081    │
   └─────────────┘       └─────────────┘       └──────┬──────┘
                                                       │
                                                       ▼
                                                ┌─────────────┐
                                                │  Inventory  │
                                                │    :8082    │
                                                └─────────────┘

          ┌─────────────────────┼─────────────────────┐
          │                     │                     │
          ▼                     ▼                     ▼
   ┌─────────────┐       ┌─────────────┐       ┌─────────────┐
   │    Cart     │       │    Order    │       │   Payment   │
   │    :8083    │       │    :8084    │       │    :8086    │
   └─────────────┘       └─────────────┘       └─────────────┘


                    ┌───────────────────────┐
                    │    Eureka Server      │
                    │         :8761         │
                    │   Service Discovery   │
                    └───────────────────────┘

                    ┌───────────────────────┐
                    │    Config Server      │
                    │         :8888         │
                    │ Centralized Config    │
                    └───────────────────────┘
```

---

## 🧩 Microservices

| Service | Port | Responsibility |
|---|---:|---|
| Config Server | `8888` | Centralized configuration |
| Eureka Server | `8761` | Service discovery |
| API Gateway | `8080` | Single entry point and request routing |
| Catalog Service | `8081` | Products, product images and variants |
| Inventory Service | `8082` | Product inventory and stock management |
| Cart Service | `8083` | Shopping cart management |
| Order Service | `8084` | Order creation and order lifecycle |
| User Service | `8085` | User and address management |
| Payment Service | `8086` | Payment records and payment status |
| Auth Service | `8087` | Registration, login and JWT authentication |

---

## 🛠️ Technology Stack

### Backend

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Spring Security
- JWT
- Maven

### Spring Cloud

- Spring Cloud Gateway
- Netflix Eureka
- Spring Cloud Config
- OpenFeign

### Database

- MySQL

### Development

- IntelliJ IDEA
- Git
- GitHub
- Postman

---

## ✨ Key Features

### Authentication

- User registration
- User login
- Password hashing
- JWT token generation
- JWT token validation
- Role information in JWT
- Protected backend APIs

### User Management

- User information management
- User roles
- Address management

### Catalog

- Product management
- Product images
- Product variants
- Product information retrieval

### Inventory

- Stock management
- Available stock calculation
- Inventory-related service communication

### Cart

- User cart
- Add cart items
- Cart item management
- Product variant association
- Stock availability checks

### Orders

- Order creation
- Order retrieval
- User-based order retrieval
- Order cancellation
- Order confirmation
- Order shipping
- Order delivery

### Payment

- Payment creation
- Payment lookup by ID
- Payment lookup by order
- Payment lookup by user
- Payment status
- Transaction ID

---

# 🔐 Authentication & Security

StyleCart uses **Spring Security and JWT** for authentication.

The authentication flow is:

```text
                  LOGIN
                    │
                    ▼
             ┌─────────────┐
             │ Auth Service│
             └──────┬──────┘
                    │
                    ▼
              JWT generated
                    │
                    ▼
                  CLIENT
                    │
             Authorization:
              Bearer <JWT>
                    │
                    ▼
             ┌─────────────┐
             │ API Gateway │
             └──────┬──────┘
                    │
              JWT validation
                    │
                    ▼
           Extract user information
                    │
          ┌─────────┼─────────┐
          ▼         ▼         ▼
       Email      Role    Internal Key
          │         │         │
          └─────────┼─────────┘
                    │
                    ▼
           Downstream Service
```

The Gateway validates the JWT before forwarding protected requests.

Authenticated user information is propagated to downstream services using internal request headers.

Example:

```text
X-User-Email
X-User-Role
X-Internal-Key
```

Downstream services validate the internal request information before allowing authenticated requests to continue.

---

# 🌐 API Gateway

The API Gateway acts as the single entry point for client requests.

Example routes:

```text
/api/auth/**              → AUTH-SERVICE
/api/users/**             → USER-SERVICE
/api/products/**          → CATALOG-SERVICE
/api/product-images/**    → CATALOG-SERVICE
/api/product-variants/**  → CATALOG-SERVICE
/api/inventory/**         → INVENTORY-SERVICE
/api/cart/**              → CART-SERVICE
/api/orders/**            → ORDER-SERVICE
/api/payments/**          → PAYMENT-SERVICE
```

Clients can therefore communicate with the system through the Gateway rather than directly calling every service.

---

# 🔎 Service Discovery

StyleCart uses **Eureka Server** for service discovery.

Instead of hardcoding service locations throughout the application, services register themselves with Eureka.

```text
                    Eureka
                     :8761
                       │
       ┌───────────────┼────────────────┐
       │               │                │
       ▼               ▼                ▼
   AUTH-SERVICE   USER-SERVICE    CATALOG-SERVICE
       │               │                │
       ▼               ▼                ▼
   CART-SERVICE    ORDER-SERVICE   PAYMENT-SERVICE
```

The API Gateway uses service names such as:

```text
lb://AUTH-SERVICE
lb://USER-SERVICE
lb://CATALOG-SERVICE
```

This allows requests to be routed using service discovery.

---

# ⚙️ Centralized Configuration

StyleCart uses **Spring Cloud Config Server** for centralized configuration.

```text
                    Config Server
                       :8888
                         │
          ┌──────────────┼──────────────┐
          ▼              ▼              ▼
      Gateway          Catalog         Cart
          │              │              │
          ▼              ▼              ▼
       Config          Config         Config
```

Services retrieve their configuration from the Config Server during startup.

This keeps configuration management separate from individual service implementations.

---

# 🔗 Inter-Service Communication

StyleCart uses **OpenFeign** for communication between services where service-to-service interaction is required.

Example:

```text
Order Service
      │
      │ OpenFeign
      ▼
Catalog Service
      │
      ▼
Product Variant
```

This allows one service to communicate with another using a declarative HTTP client.

---

# 🛒 Main E-Commerce Flow

A simplified StyleCart flow is:

```text
User
 │
 ▼
Register
 │
 ▼
Login
 │
 ▼
JWT
 │
 ▼
API Gateway
 │
 ▼
Browse Products
 │
 ▼
Check Inventory
 │
 ▼
Add Product to Cart
 │
 ▼
Create Order
 │
 ▼
Payment
 │
 ▼
Order Lifecycle
 │
 ├── PENDING
 ├── CONFIRMED
 ├── SHIPPED
 └── DELIVERED
```

---

# 📦 Project Structure

```text
StyleCart-Microservices/
│
├── config-server/
│
├── eureka-server/
│
├── api-gateway/
│
├── auth-service/
│
├── user-service/
│
├── catalog-service/
│
├── inventory-service/
│
├── cart-service/
│
├── order-service/
│
├── payment-service/
│
├── README.md
│
└── .gitignore
```

Individual services follow a layered Spring Boot structure where appropriate:

```text
src/
└── main/
    ├── java/
    │   └── com.stylecart/
    │       ├── controller/
    │       ├── service/
    │       ├── repository/
    │       ├── entity/
    │       ├── dto/
    │       ├── config/
    │       ├── exception/
    │       └── security/
    │
    └── resources/
        └── application.yaml
```

---

# 🗄️ Database

StyleCart uses **MySQL** with Spring Data JPA and Hibernate.

The services use separate database/schema areas according to their responsibilities.

Examples include:

```text
User data
Catalog data
Inventory data
Cart data
Order data
Payment data
Authentication data
```

This keeps service responsibilities separated at the application level.

---

# 🚀 Getting Started

## Prerequisites

Make sure the following are installed:

- Java 21
- Maven
- MySQL
- Git
- IntelliJ IDEA or another Java IDE
- Postman or another API client

---

## 1. Clone the repository

```bash
git clone <YOUR-GITHUB-REPOSITORY-URL>
```

```bash
cd StyleCart-Microservices
```

---

## 2. Configure MySQL

Create the required databases according to the configuration used by each service.

Update the database credentials in the corresponding configuration files if required.

Example:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/<database>
    username: <username>
    password: <password>
```

---

## 3. Start Config Server

Start:

```text
config-server
```

Port:

```text
8888
```

---

## 4. Start Eureka Server

Start:

```text
eureka-server
```

Port:

```text
8761
```

Open:

```text
http://localhost:8761
```

The Eureka dashboard should display the registered services.

---

## 5. Start the remaining services

Start:

```text
api-gateway
auth-service
user-service
catalog-service
inventory-service
cart-service
order-service
payment-service
```

Once running, the services should register with Eureka.

---

# 🧪 Testing Through API Gateway

The recommended way to access the backend is through:

```text
http://localhost:8080
```

For example:

```text
POST http://localhost:8080/api/auth/register
```

```text
POST http://localhost:8080/api/auth/login
```

After successful login, the returned JWT can be used for protected endpoints.

Example:

```text
Authorization: Bearer <JWT>
```

---

# 🔑 Example Authentication Flow

### Register

```http
POST /api/auth/register
Content-Type: application/json
```

Example request:

```json
{
  "email": "user@example.com",
  "password": "password",
  "firstName": "John",
  "lastName": "Doe",
  "phone": 9876543210
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json
```

Example:

```json
{
  "email": "user@example.com",
  "password": "password"
}
```

The Auth Service returns a JWT that can be used for protected requests.

---

# 🔒 Protected Requests

For protected endpoints:

```http
Authorization: Bearer <JWT>
```

Example:

```http
GET /api/cart/1
```

```http
GET /api/orders/1
```

The API Gateway validates the JWT before forwarding the request.

---

# 📊 Service Ports

| Component | Port |
|---|---:|
| API Gateway | 8080 |
| Catalog Service | 8081 |
| Inventory Service | 8082 |
| Cart Service | 8083 |
| Order Service | 8084 |
| User Service | 8085 |
| Payment Service | 8086 |
| Auth Service | 8087 |
| Eureka Server | 8761 |
| Config Server | 8888 |

---

# 🎯 What I Learned From This Project

This project was built to gain practical experience with:

- Designing a microservices architecture
- Breaking a monolithic-style application into independent services
- Service discovery using Eureka
- Centralized configuration
- API Gateway routing
- REST API development
- Spring Data JPA and Hibernate
- MySQL database integration
- JWT authentication
- Spring Security
- Role-based authentication
- Inter-service communication
- OpenFeign
- Exception handling
- DTO-based API design
- Testing APIs with Postman
- Managing a multi-module backend project with Git and GitHub

---

# 🔮 Future Improvements

The project is intentionally focused on learning core Spring Boot microservices concepts.

Possible future improvements include:

- Redis caching
- Kafka/event-driven communication
- Docker and Docker Compose
- Circuit breakers and resilience patterns
- Distributed tracing
- CI/CD
- Cloud deployment
- More comprehensive automated testing

These are **future learning possibilities** and are not currently part of the implemented StyleCart system.

---

# 👨‍💻 Project Purpose

StyleCart was built as a hands-on backend project to understand how modern Java backend applications can be designed using Spring Boot and microservices.

The project focuses on practical implementation rather than simply demonstrating individual technologies.

---

## Author

**Harsh Saroha**

Java Backend Developer | Spring Boot | Microservices

GitHub: `Harshsaroha0`

---

## License

This project is created for learning and portfolio purposes.
