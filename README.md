# Spring Microservices Demo

A demonstration of two Spring Boot microservices communicating via Apache Kafka.

## Services

### Order Service (port 8081)
Manages customer orders. When an order is created, it publishes an `OrderCreated` event to Kafka and waits for an inventory response to confirm or reject the order.

**Endpoints:**
- `POST /api/orders` — Create a new order
- `GET /api/orders` — List all orders
- `GET /api/orders/{id}` — Get order by ID

### Inventory Service (port 8082)
Manages product inventory. Listens for `OrderCreated` events, checks stock availability, reserves items, and publishes a response back to the Order Service.

**Endpoints:**
- `GET /api/inventory` — List all inventory items
- `PUT /api/inventory/{productId}/restock` — Restock a product

## Tech Stack

- Java 17
- Spring Boot 3.2.4
- Spring Kafka
- PostgreSQL
- Apache Kafka (Confluent 7.6.x)
- Maven (multi-module)

## Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

## Getting Started

1. **Start infrastructure:**
   ```bash
   docker-compose up -d
   ```

2. **Build the project:**
   ```bash
   mvn clean package
   ```

3. **Run Order Service:**
   ```bash
   cd order-service
   mvn spring-boot:run
   ```

4. **Run Inventory Service:**
   ```bash
   cd inventory-service
   mvn spring-boot:run
   ```

## Usage

**Create an order:**
```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-001",
    "productId": "PROD-001",
    "quantity": 2,
    "totalPrice": 49.99
  }'
```

**Check inventory:**
```bash
curl http://localhost:8082/api/inventory
```

**Restock a product:**
```bash
curl -X PUT "http://localhost:8082/api/inventory/PROD-001/restock?quantity=100"
```

## Architecture

```
Order Service  --[OrderCreated]--> Kafka --> Inventory Service
                                              |
Order Service <--[InventoryResponse]-- Kafka <-+
```

The Order Service creates an order with `PENDING` status and publishes an event. The Inventory Service checks availability and responds. The Order Service then updates the status to `CONFIRMED` or `REJECTED`.
