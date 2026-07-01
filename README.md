# Notification Service

## Overview
A Spring Boot microservice for sending push notifications. It supports secure bank authentication, robust error handling, and is ready for containerized deployment.

---

## Features
- Send push notifications to devices
- Bank authentication via headers (x-bank-id, x-bank-secret)
- Robust error handling with detailed messages and HTTP status codes
- Logging for all operations and errors
- Docker and Kubernetes ready

---

## Prerequisites
- Java 21
- Maven
- PostgreSQL
- Docker & Docker Compose (for containerized deployment)

---

## Environment Variables & Configuration

Set the following in your `application.properties`:

```
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/notificationdb
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update

# Server port
server.port=8082
```

---

## Database Setup

### Bank Secrets Table
```sql
CREATE TABLE bank_secrets (
    bank_id VARCHAR(50) PRIMARY KEY,
    bank_secret VARCHAR(255) NOT NULL
);
```

---

## Running Locally

### 1. Build
```
mvn clean package
```

### 2. Run (local)
```
java -jar target/notification-service-4.1.1.jar
```

### 3. Run with Docker
```
docker build -t notification-service .
docker run -p 8082:8082 \
  --env SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/notificationdb \
  --env SPRING_DATASOURCE_USERNAME=your_db_user \
  --env SPRING_DATASOURCE_PASSWORD=your_db_password \
  notification-service
```

### 4. Run with Docker Compose
See `docker-compose.yml` for environment setup.

---

## API Endpoints

### Send Notification
- **POST** `/notify`
- **Headers:**
  - `Content-Type: application/json`
  - `x-bank-id: <bank_id>`
  - `x-bank-secret: <bank_secret>`
- **Body:**
```json
{
  "cpfNumber": "1234567890",
  "request": {
    "notification": { "title": "...", "body": "..." },
    "data": { "key": "value" }
  }
}
```
- **Response:**
  - `200 OK` on success
  - `401 Unauthorized` if bank credentials invalid
  - `500 Internal Server Error` for other failures

---

## Error Handling
- All errors return a JSON body with `error` and `cause` fields.
- Global exception handler ensures consistent error responses.
- Example:
```json
{
  "error": "Unexpected error",
  "cause": "Push notification provider not configured"
}
```

---

## Logging
- All requests, responses, and errors are logged with context.
- Sensitive data (like secrets) is never logged.

---

## Deployment Notes
- Never commit secret files to version control (see `.gitignore`).
- Expose port 8082.

---

## Security Notes
- Bank authentication is enforced for notification requests.

---

## Contribution & License
- PRs welcome. Please follow code style and add tests.
- License: MIT (or your choice)

---

## Contact
For issues, open a GitHub issue or contact the maintainer.
