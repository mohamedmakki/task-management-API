# Task Management API - DevOps demonstration

**Made by:** Mohamed MEKKI - Groupe C

The Task Management API is a RESTful web service built with Spring Boot that provides task management functionality.

The goal of this project is to demonstrate DevOps practices in a practical project.

---

## Table of Contents

- [Technologies & Tools](#technologies--tools)
  - [Backend Framework](#backend-framework)
  - [Dependencies & Libraries](#dependencies--libraries)
  - [Observability & Monitoring](#observability--monitoring)
  - [CI/CD](#cicd)
  - [Containerization & Deployment](#containerization--deployment)
  - [Security & Testing](#security--testing)
- [Steps to Run Locally](#steps-to-run-locally)
  - [1. Clone the repository](#1clone-the-repository)
  - [2. Run the application](#2run-the-application)
  - [3. Verify the application](#3verify-the-application)
  - [Configuration](#configuration)
- [Docker Setup](#docker-setup)
  - [Option 1 - Using Docker Compose (recommended)](#option-1---using-docker-compose-recommended)
  - [Option 2 - Using Dockerfile](#option-2---using-dockerfile)
  - [Option 3 - Pulling from Docker Hub](#option-3---pulling-from-docker-hub)
- [Endpoints and API Examples](#endpoints-and-api-examples)
  - [Validation Rules](#validation-rules)
  - [API examples](#base-url)

---

## Technologies & Tools

### Backend Framework
- **Spring Boot 3.5.9** - Main application framework
- **Java 17** - Programming language
- **Maven** - Build automation and dependency management
- **H2** - Embedded in-memory database

### Dependencies & Libraries
- **Spring Boot Starter Web** - RESTful web services
- **Spring Boot Starter Data JPA** - Database operations
- **Spring Boot Starter Validation** - Input validation
- **Spring Boot Starter Actuator** - Application monitoring and health checks
- **H2 Database** - In-memory database
- **Lombok** - Reduce boilerplate code

### Observability & Monitoring
- **Logback**  - Structured logging for centralized log ingestion
- **Micrometer Tracing** - Distributed tracing support (bridge to tracing systems)
- **Prometheus** - Metrics registry and scraping (Prometheus dashboard not implemented in this repository)

### CI/CD
- **GitHub Actions** - Continuous Integration and Continuous Deployment 
	- Included workflows: automated build & test, Docker image build/publish, DAST scan, SAST scans

### Containerization & Deployment
- **Docker** - Container platform 
- **Docker Compose** - Multi-container orchestration
- **Docker Hub** - Container registry used in CI/CD workflows for publishing images
- **QEMU & Docker Buildx** - Multi-platform image builds, caching and automated pushing to container registery

### Security & Testing

- **SAST (Static Application Security Testing)**
  
  - **Gitleaks** - Scan for hardcoded secrets
  - **SonarCloud** - Code quality analysis (GitHub App)

- **DAST (Dynamic Application Security Testing)**
  
  - **OWASP ZAP (zaproxy/action-baseline)** - Passive secuirty scanning of the deployed API
  - **Spring Boot Test** - Unit testing using built in Maven test

---

## Steps to Run Locally

### 1.Clone the repository

Bash

```bash
git clone https://github.com/mohamedmakki/task-management-API.git
cd task-management-API
```

PowerShell

```powershell
git clone https://github.com/mohamedmakki/task-management-API.git
Set-Location task-management-API
```

### 2.Run the application

Using the bundled Maven wrapper

Bash

```bash
./mvnw spring-boot:run
```

PowerShell

```powershell
.\mvnw spring-boot:run
```

### 3.Verify the application

Check the health endpoint (Bash / macOS / Linux):

```bash
curl http://localhost:8080/actuator/health
```

PowerShell (Windows):

```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
```

You should receive a JSON response indicating `status: UP` when the application is ready.

#### Configuration

- **Port**: `8080`

- **Database**: H2 file-based database stored in `./data/taskDB`

- **Logs**: Stored in `./logs` (max 10MB per file with rotation)

- **Metrics**: Available at `/actuator/prometheus` and `/actuator/metrics`

---

## Docker Setup

### Option 1 - Using Docker Compose (recommended)

Docker Compose will build and run a container with the same default configuration as the previous section:

```bash
# Clone the repository
git clone https://github.com/mohamedmakki/task-management-API.git. git

# Make sure you are in the root directory of the project
cd task-management-API

#Run in attached mode
docker compose up

#Or run in detached mode 
docker compose up -d

#Stop the container
docker compose down

#Force rebuild the contanier if you change anything in source code
docker compose up --build
```

> [!NOTE]
> 
> You can override server `PORT`, `/data` and `/logs` directories by creating a `.env` file:

```bash
cp .env.example .env    # Linux/Mac/Git Bash
copy .env.example .env  # Windows
```

Edit `.env`:

```env
APP_PORT=8080
LOG_PATH=./logs
DATA_PATH=./data
```

**Run:**

```bash
docker compose up
```

### Option 2 - Using Dockerfile

Build the Docker image:

```bash
docker build -t task-management-api .
```

Container basic run :

```bash
docker run -p 8080:8080 task-management-api:local
```

Run container  with persistent logs and database:

```bash
docker run -p 8080:8080 \
  -v $(pwd)/logs:/logs \
  -v $(pwd)/data:/data \
  task-management-api:local
```

For Windows PowerShell:

```powershell
docker run -p 8080:8080 `
  -v "${PWD}\logs:/logs" `
  -v "${PWD}\data:/data" `
  task-management-api:local
```

### Option 3 - Pulling from Docker Hub

Pull the latest image from Docker Hub:

```bash
docker pull mekk1mohamed/task-management-api:latest
```

Basic run :

```bash
docker run -p 8080:8080 mekk1mohamed/task-management-api:latest
```

Run with persistent logs and database:

```bash
docker run -p 8080:8080 \
  -v $(pwd)/logs:/logs \
  -v $(pwd)/data:/data \
  mekk1mohamed/task-management-api:latest
```

For Windows PowerShell:

```powershell
docker run -p 8080:8080 `
  -v "${PWD}\logs:/logs" `
  -v "${PWD}\data:/data" `
  mekk1mohamed/task-management-api:latest
```
---

## Endpoints and API Examples

The API provides the following REST endpoints for task management:

### Validation Rules

| Field | Required | Type | Constraints |
|-------|----------|------|-------------|
| **title** | Yes | String | Maximum 255 characters |
| **description** | No | String | Maximum 500 characters |
| **completed** | Yes | Boolean | Must be `true` or `false` |


### Base URL
```
http://localhost:8080
```

### 1.Get All Tasks

Retrieves a list of all tasks in the system.

**Endpoint:** `GET http://localhost:8080/tasks`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Complete project documentation",
    "description": "Write comprehensive README and API documentation",
    "completed": false,
    "createdAt": "2024-01-15T10:00:00",
    "updatedAt": "2024-01-15T10:00:00"
  }
]
```

### 2.Create a New Task

Creates a new task with the provided details.

**Endpoint:** `POST http://localhost:8080/tasks`

**Request Body:**
```json
{
  "title": "New Task Title",
  "description": "Task description (optional)",
  "completed": false
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "title": "New Task Title",
  "description": "Task description (optional)",
  "completed": false,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### 3.Get Task by ID

Retrieves a specific task by its unique ID.

**Endpoint:** `GET http://localhost:8080/tasks/{id}`

**Example:** `GET http://localhost:8080/tasks/1`

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Complete project documentation",
  "description": "Write comprehensive README and API documentation",
  "completed": false,
  "createdAt": "2024-01-15T10:00:00",
  "updatedAt": "2024-01-15T10:00:00"
}
```

### 4. Update Task

Updates an existing task with new information.

**Endpoint:** `PUT http://localhost:8080/tasks/{id}`

**Example:** `PUT http://localhost:8080/tasks/1`

**Request Body:**
```json
{
  "title": "Updated Task Title",
  "description": "Updated description",
  "completed": true
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Updated Task Title",
  "description": "Updated description",
  "completed": true,
  "createdAt": "2024-01-15T10:00:00",
  "updatedAt": "2024-01-15T10:45:00"
}
```

### 5.Delete Task

Permanently deletes a task from the system.

**Endpoint:** `DELETE http://localhost:8080/tasks/{id}`

**Example:** `DELETE http://localhost:8080/tasks/1`

**Response:** `204 No Content`





