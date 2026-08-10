# 🚀 Real-Time Chat Application Backend

A scalable, high-performance, real-time chat application backend built with **Spring Boot**, **Spring WebSocket (STOMP)**, **Spring Data JPA**, and **PostgreSQL**.

---

## 🌟 Key Features

- **⚡ Real-Time Messaging**: Bi-directional communication using WebSocket with STOMP protocol over SockJS and direct WebSocket connections.
- **🏷️ Multi-Room Architecture**: Dynamic creation, management, and deletion of custom chat rooms.
- **💬 Event Types Support**: Dedicated message handling for `CHAT`, `JOIN`, and `LEAVE` room events.
- **⚡ Optimized Persistence & Querying**:
  - Independent `MessageRepository` for instant single-record persistence (eliminates memory bottlenecks).
  - Database-level SQL pagination (`Pageable`) for efficient chat history retrieval.
  - Multi-column indexes on `(room_id, timeStamp)` and unique room constraints.
- **🛡️ Clean Architecture & DTO Pattern**: Clean separation of Concerns (`Controller` ➔ `Service` ➔ `Repository` ➔ `Entity`), completely preventing circular JSON recursion.
- **⚠️ Centralized Exception Handling**: Uniform REST error payloads with field-level validation errors via `@RestControllerAdvice`.
- **🌐 Dynamic Cross-Origin Resource Sharing (CORS)**: Configurable CORS support for React, Next.js, Vue, and Angular clients.
- **🧪 100% Automated Test Coverage**: Comprehensive unit and integration test suite using Mockito and Spring MockMvc.

---

## 🛠️ Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 21 |
| **Framework** | Spring Boot (v4.x / 3.x) |
| **Data & ORM** | Spring Data JPA / Hibernate ORM |
| **Real-Time Protocol** | Spring WebSocket + STOMP + SockJS |
| **Database** | PostgreSQL (Production / Dev), H2 (Automated Unit Tests) |
| **Build Tool** | Apache Maven (with `mvnw` wrapper) |
| **Validation** | Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@Size`) |
| **Testing** | JUnit 5, Mockito, Spring Boot Test, Spring WebMvcTest |

---

## 📁 Project Structure

```
chat-app-backend/
├── src/
│   ├── main/
│   │   ├── java/com/chatapp/
│   │   │   ├── ChatAppBackendApplication.java    # Application Main Class
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java               # Global CORS Configuration
│   │   │   │   └── WebSocketConfig.java          # STOMP WebSocket Endpoints & Broker
│   │   │   ├── controller/
│   │   │   │   ├── ChatController.java           # STOMP WebSocket Message Mapping
│   │   │   │   └── RoomController.java           # REST Controller for Chat Rooms
│   │   │   ├── entity/
│   │   │   │   ├── Message.java                  # JPA Entity with Indexing & Constraints
│   │   │   │   ├── MessageType.java              # CHAT, JOIN, LEAVE Enum
│   │   │   │   └── Room.java                     # JPA Entity with Unique Room Index
│   │   │   ├── exception/
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── GlobalExceptionHandler.java   # Centralized Exception Handler
│   │   │   │   ├── ResourceAlreadyExistsException.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── payload/
│   │   │   │   ├── ApiResponse.java              # Generic Response Envelope
│   │   │   │   ├── CreateRoomRequest.java        # Validated Room Creation Request
│   │   │   │   ├── ErrorResponse.java            # Standardized Error Response
│   │   │   │   ├── MessageDto.java               # Clean Message DTO
│   │   │   │   ├── MessageRequest.java           # Incoming Message Request
│   │   │   │   ├── PageResponse.java             # Standard Pagination Wrapper
│   │   │   │   └── RoomDto.java                  # Room DTO with Total Message Count
│   │   │   ├── repository/
│   │   │   │   ├── MessageRepository.java        # JPA Repository with DB Pagination
│   │   │   │   └── RoomRepository.java           # JPA Repository for Rooms
│   │   │   └── service/
│   │   │       ├── ChatService.java
│   │   │       ├── RoomService.java
│   │   │       └── impl/
│   │   │           ├── ChatServiceImpl.java
│   │   │           └── RoomServiceImpl.java
│   │   └── resources/
│   │       └── application.properties            # PostgreSQL & Server Configuration
│   └── test/
│       ├── java/com/chatapp/
│       │   ├── ChatAppBackendApplicationTests.java
│       │   ├── controller/
│       │   │   └── RoomControllerTest.java       # MockMvc Endpoint Tests
│       │   └── service/
│       │       ├── ChatServiceTest.java          # Chat Logic Unit Tests
│       │       └── RoomServiceTest.java          # Room Logic Unit Tests
│       └── resources/
│           └── application-test.properties       # H2 In-Memory DB for Tests
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

### 1. Prerequisites
- **JDK 21** or later installed.
- **PostgreSQL** running locally (or via Docker).
- **Maven** (optional, Maven Wrapper `mvnw` included).

### 2. Database Setup
Create a PostgreSQL database named `chat_app`:
```sql
CREATE DATABASE chat_app;
```

### 3. Configure `application.properties`
Update `src/main/resources/application.properties` with your PostgreSQL credentials:

```properties
spring.application.name=chat-app-backend
server.port=8080

# PostgreSQL Database
spring.datasource.url=jdbc:postgresql://localhost:5432/chat_app
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.open-in-view=false

# Allowed CORS Origins
app.cors.allowed-origins=http://localhost:5173,http://localhost:3000,http://localhost:8080
```

### 4. Build & Run Application
Run using the Maven Wrapper:

**Windows (PowerShell / CMD):**
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

The application will start on: `http://localhost:8080`

---

## 📡 REST API Documentation

Base URL: `http://localhost:8080/api/v1/rooms`

### 1. Create a Room
- **Method**: `POST`
- **Path**: `/api/v1/rooms`
- **Request Body**:
  ```json
  {
    "roomId": "developers"
  }
  ```
- **Response**: `201 Created`
  ```json
  {
    "id": 1,
    "roomId": "developers",
    "createdAt": "2026-08-10T12:00:00",
    "totalMessages": 0
  }
  ```

### 2. Get Room by ID
- **Method**: `GET`
- **Path**: `/api/v1/rooms/{roomId}`
- **Response**: `200 OK`
  ```json
  {
    "id": 1,
    "roomId": "developers",
    "createdAt": "2026-08-10T12:00:00",
    "totalMessages": 45
  }
  ```

### 3. List All Rooms (Paginated)
- **Method**: `GET`
- **Path**: `/api/v1/rooms?page=0&size=50`
- **Response**: `200 OK`
  ```json
  {
    "content": [
      {
        "id": 1,
        "roomId": "developers",
        "createdAt": "2026-08-10T12:00:00",
        "totalMessages": 45
      }
    ],
    "pageNumber": 0,
    "pageSize": 50,
    "totalElements": 1,
    "totalPages": 1,
    "lastPage": true
  }
  ```

### 4. Get Room Message History (Paginated)
- **Method**: `GET`
- **Path**: `/api/v1/rooms/{roomId}/messages?page=0&size=20`
- **Response**: `200 OK`
  ```json
  [
    {
      "id": 101,
      "sender": "Alice",
      "content": "Hello everyone!",
      "timeStamp": "2026-08-10T12:05:00",
      "roomId": "developers",
      "messageType": "CHAT"
    }
  ]
  ```

### 5. Delete a Room
- **Method**: `DELETE`
- **Path**: `/api/v1/rooms/{roomId}`
- **Response**: `200 OK`
  ```json
  {
    "success": true,
    "message": "Room deleted successfully",
    "data": "developers",
    "timestamp": "2026-08-10T12:10:00"
  }
  ```

---

## ⚡ WebSocket & STOMP Protocol

### 1. Connection Endpoints
- **SockJS Endpoint**: `http://localhost:8080/chat`
- **Native WebSocket Endpoint**: `ws://localhost:8080/chat-raw`

### 2. Channels & Destinations

| Action | Destination | Description |
| :--- | :--- | :--- |
| **Subscribe** | `/topic/room/{roomId}` | Listen to real-time messages in the specified room |
| **Publish** | `/app/sendMessage/{roomId}` | Send a message to the specified room |

### 3. Message Payloads

#### Send Message (Client ➔ Server):
```json
{
  "sender": "Alice",
  "content": "Hello team!",
  "roomId": "developers",
  "messageType": "CHAT"
}
```
*(Optional `messageType` values: `CHAT`, `JOIN`, `LEAVE`)*

#### Receive Message (Server ➔ Client):
```json
{
  "id": 102,
  "sender": "Alice",
  "content": "Hello team!",
  "timeStamp": "2026-08-10T12:06:00",
  "roomId": "developers",
  "messageType": "CHAT"
}
```

---

## 🧪 Running Automated Tests

Run the complete test suite (executes using in-memory H2 database, independent of PostgreSQL):

```bash
# Windows
.\mvnw.cmd test

# Linux / Mac
./mvnw test
```

### Test Summary:
- `ChatAppBackendApplicationTests`: Spring Boot context load verification.
- `RoomServiceTest`: Room creation, conflict handling, retrieval, and message pagination.
- `ChatServiceTest`: Direct message persistence, input validation, and room association.
- `RoomControllerTest`: MockMvc integration tests for all REST endpoints and status codes.

---

## 📄 License
This project is licensed under the MIT License.
