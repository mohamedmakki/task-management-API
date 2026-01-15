# Task Management API - DevOps demonstration

**Made by:** Mohamed MEKKI - Groupe C

The Task Management API is a RESTful web service built with Spring Boot that provides task management functionality.

The goal of this project is to demonstrate DevOps practices in a practical project.

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

