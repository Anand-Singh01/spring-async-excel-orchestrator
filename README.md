# 📊 Excel Orchestrator – Real-Time File Processing

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![React](https://img.shields.io/badge/React-18-blue)
![WebSocket](https://img.shields.io/badge/WebSocket-Real%20Time-orange)

## 🚀 Overview

Excel Orchestrator is a full-stack application that enables users to upload Excel files,
process them asynchronously on the backend, persist data into a database,
and receive real-time progress updates via WebSockets.

It uses a task-based progress tracking system so users can monitor file processing live.

---

## ✨ Features

✅ Upload Excel (.xlsx) files  
✅ Async backend processing  
✅ Database persistence  
✅ Real-time progress tracking  
✅ Task-based subscription model  
✅ WebSocket communication (STOMP + SockJS)  
✅ Clean modern UI with progress bar  

---

## 🏗 Architecture

### Backend
- Spring Boot
- Spring Web
- Spring WebSocket
- Spring Async
- Apache POI
- JPA / Database

### Frontend
- React (TypeScript)
- TailwindCSS
- STOMP Client
- SockJS

### Real-Time Flow

User Upload → Backend Generates Task → Async Processing →  
Progress Sent To `/topic/progress/{taskId}` →  
Frontend Updates Progress Bar

---

## ⚙️ Setup & Installation

### 🔧 Backend

1. Clone repository  
2. Configure database in `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dbname
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

3. Enable Async

```java
@EnableAsync
@SpringBootApplication
public class ExcelOrchestratorApplication {
}
```

4. Run backend:

```bash
mvn spring-boot:run
```

Backend runs at:

```
http://localhost:8080
```

---

### 🎨 Frontend

1. Install dependencies

```bash
npm install
```

2. Install required packages

```bash
npm install stompjs sockjs-client uuid
```

3. Start frontend

```bash
npm run dev
```

Frontend runs at:

```
http://localhost:3000
```

---

## 📡 WebSocket Configuration

Backend WebSocket endpoint:

```
ws://localhost:8080/ws-excel
```

Topic:

```
/topic/progress/{taskId}
```

---

## 📂 Project Structure

### Backend

```
backend/
 ├── controller/
 ├── service/
 ├── config/
 ├── model/
 ├── repository/
```

### Frontend

```
frontend/
 ├── api/
 ├── components/
 ├── pages/
```

---

## 🔥 Example Progress

```
0%
15%
32%
64%
84%
100%
```

Task automatically completes and unsubscribes.

---

## 🚀 Future Improvements

- Batch database inserts
- Task history dashboard
- Authentication
- File validation + better error handling
- Docker support
- Cloud deployment

---

## 🧑‍💻 Author

Built with ❤️ using Spring Boot + React + WebSockets

---
