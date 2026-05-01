# Spring Boot CRUD Application

A complete Spring Boot REST API application with full CRUD operations, layered architecture, validation, exception handling, and CI/CD pipeline integration with Jenkins.

## 📋 Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Getting Started](#getting-started)
- [Running with Maven](#running-with-maven)
- [Running with Docker](#running-with-docker)
- [CI/CD Pipeline](#cicd-pipeline)
- [Jenkins Setup](#jenkins-setup)
- [Testing](#testing)
- [Environment Variables](#environment-variables)
- [GitHub Webhook Integration](#github-webhook-integration)

## ✨ Features

- **Complete CRUD Operations**: Create, Read, Update, Delete users
- **RESTful API**: Standard REST API design with proper HTTP methods and status codes
- **Layered Architecture**: Controller → Service → Repository → Entity
- **JPA/Hibernate**: Database persistence with H2 (development) and MySQL (production) support
- **Validation**: Input validation using Bean Validation API
- **Exception Handling**: Global exception handler with consistent error responses
- **Lombok**: Reduced boilerplate code
- **Unit Testing**: JUnit 5 and Mockito for comprehensive test coverage
- **CI/CD Pipeline**: Jenkins pipeline for automated build, test, and deployment
- **Docker Support**: Multi-stage Dockerfile for containerization
- **Docker Compose**: Orchestration for local development

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Client (Browser/Postman)                 │
└───────────────────────┬───────────────────────────────────────┘
                        │ HTTP Requests
┌───────────────────────▼───────────────────────────────────────┐
│              UserController (REST Controller)                │
│              @RestController                                 │
│              Base Path: /api/v1/users                        │
└───────────────────────┬───────────────────────────────────────┘
                        │ Service Layer
┌───────────────────────▼───────────────────────────────────────┐
│              UserService (Business Logic)                    │
│              @Service                                        │
│              Transaction Management                          │
└───────────────────────┬───────────────────────────────────────┘
                        │ Repository Layer
┌───────────────────────▼───────────────────────────────────────┐
│              UserRepository (Data Access)                    │
│              @Repository extends JpaRepository               │
│              Custom Query Methods                            │
└───────────────────────┬───────────────────────────────────────┘
                        │ JPA/Hibernate
┌───────────────────────▼───────────────────────────────────────┐
│              User Entity                                     │
│              @Entity @Table                                  │
│              Validation Annotations                          │
└───────────────────────┬───────────────────────────────────────┘
                        │
┌───────────────────────▼───────────────────────────────────────┐
│              H2 Database (Dev) / MySQL (Prod)               │
└─────────────────────────────────────────────────────────────┘
```

## 🛠️ Technology Stack

| Category | Technology |
|----------|------------|
| **Framework** | Spring Boot 3.2.5 |
| **Language** | Java 17 |
| **Build Tool** | Maven 3.9.x |
| **Database** | H2 (Dev), MySQL (Prod) |
| **ORM** | JPA / Hibernate |
| **Validation** | Jakarta Bean Validation |
| **Testing** | JUnit 5, Mockito |
| **CI/CD** | Jenkins |
| **Container** | Docker, Docker Compose |

## 📁 Project Structure

```
crud-application/
├── src/
│   ├── main/
│   │   ├── java/com/devops/crud/
│   │   │   ├── CrudApplication.java          # Main Application Class
│   │   │   ├── controller/
│   │   │   │   └── UserController.java       # REST API Controller
│   │   │   ├── service/
│   │   │   │   └── UserService.java          # Business Logic Layer
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java       # Data Access Layer
│   │   │   ├── entity/
│   │   │   │   └── User.java                 # JPA Entity
│   │   │   ├── dto/
│   │   │   │   ├── UserDTO.java              # Data Transfer Object
│   │   │   │   └── ApiResponse.java          # API Response Wrapper
│   │   │   └── exception/
│   │   │       ├── UserNotFoundException.java    # Custom Exception
│   │   │       ├── DuplicateEmailException.java  # Custom Exception
│   │   │       └── GlobalExceptionHandler.java     # Exception Handler
│   │   └── resources/
│   │       └── application.properties        # Application Configuration
│   └── test/
│       ├── java/com/devops/crud/
│       │   ├── CrudApplicationTests.java     # Integration Tests
│       │   ├── controller/
│       │   │   └── UserControllerTest.java   # Controller Unit Tests
│       │   ├── service/
│       │   │   └── UserServiceTest.java      # Service Unit Tests
│       │   └── repository/
│       │       └── UserRepositoryTest.java   # Repository Tests
│       └── resources/
│           └── application-test.properties   # Test Configuration
├── Jenkinsfile                               # Jenkins Pipeline
├── Dockerfile                                # Docker Image Build
├── docker-compose.yml                        # Docker Compose Config
├── .dockerignore                             # Docker Ignore Rules
├── pom.xml                                   # Maven Configuration
└── README.md                                 # Project Documentation
```

## 🔌 API Endpoints

### Base URL
```
http://localhost:8080/api/v1/users
```

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/users` | Create a new user |
| `GET` | `/api/v1/users` | Get all users |
| `GET` | `/api/v1/users/{id}` | Get user by ID |
| `GET` | `/api/v1/users/email/{email}` | Get user by email |
| `PUT` | `/api/v1/users/{id}` | Update user by ID |
| `DELETE` | `/api/v1/users/{id}` | Delete user by ID |
| `GET` | `/api/v1/users/search/firstname/{firstName}` | Search by first name |
| `GET` | `/api/v1/users/search/lastname/{lastName}` | Search by last name |
| `GET` | `/api/v1/users/age/greater-than/{age}` | Get users by age |
| `GET` | `/api/v1/users/health` | Health check endpoint |

### Request/Response Examples

#### Create User
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "age": 30,
    "address": "123 Main St"
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "age": 30,
    "address": "123 Main St",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

#### Get All Users
```bash
curl http://localhost:8080/api/v1/users
```

#### Update User
```bash
curl -X PUT http://localhost:8080/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Doe",
    "email": "jane.doe@example.com",
    "phone": "0987654321",
    "age": 28,
    "address": "456 Oak St"
  }'
```

#### Delete User
```bash
curl -X DELETE http://localhost:8080/api/v1/users/1
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.9.x or higher
- Docker (optional, for containerized deployment)
- Jenkins (for CI/CD pipeline)

## 🏃 Running with Maven

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/crud-application.git
cd crud-application
```

### 2. Build the Project
```bash
mvn clean compile
```

### 3. Run Tests
```bash
mvn test
```

### 4. Package the Application
```bash
mvn clean package
```

### 5. Run the Application
```bash
# Run the JAR file
java -jar target/crud-application-1.0.0.jar

# Or run with Maven
mvn spring-boot:run
```

### 6. Access the Application
- Application URL: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:cruddb`
  - Username: `sa`
  - Password: (leave empty)

## 🐳 Running with Docker

### Build Docker Image
```bash
docker build -t crud-application:1.0.0 .
```

### Run Docker Container
```bash
# Run with default configuration
docker run -p 8080:8080 crud-application:1.0.0

# Run with custom port
docker run -p 9090:8080 -e PORT=8080 crud-application:1.0.0

# Run in detached mode
docker run -d -p 8080:8080 --name crud-app crud-application:1.0.0
```

### Using Docker Compose
```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop services
docker-compose down

# Rebuild and start
docker-compose up -d --build
```

## 🔧 CI/CD Pipeline

### Jenkins Pipeline Overview

The Jenkinsfile defines a multi-stage pipeline:

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│    Clone     │───▶│    Build     │───▶│     Test     │───▶│   Package    │
│  Repository  │    │    Project   │    │    Tests     │    │  Application │
└──────────────┘    └──────────────┘    └──────────────┘    └──────────────┘
                                                                   │
                                                                   ▼
                                                          ┌──────────────┐
                                                          │   Archive    │
                                                          │   Artifacts  │
                                                          └──────────────┘
                                                                   │
                                                                   ▼
                                                          ┌──────────────┐
                                                          │Build Docker  │
                                                          │    Image     │
                                                          └──────────────┘
```

### Pipeline Stages

1. **Clone Repository** - Pulls latest code from GitHub
2. **Build Project** - Compiles the code using Maven
3. **Run Tests** - Executes unit tests and generates reports
4. **Code Quality** - Optional SonarQube analysis
5. **Package Application** - Creates executable JAR file
6. **Archive Artifacts** - Stores JAR and test reports
7. **Build Docker Image** - Creates container image (main branch only)
8. **Deploy** - Deploys to target environment (main branch only)

## 🔨 Jenkins Setup

### 1. Install Required Plugins
- Git Plugin
- Maven Integration Plugin
- Pipeline Plugin
- JUnit Plugin
- JaCoCo Plugin
- Docker Pipeline Plugin (optional)
- GitHub Integration Plugin

### 2. Configure Tools
1. **Manage Jenkins** → **Global Tool Configuration**
2. Add JDK 17
3. Add Maven 3.9.x
4. Add Docker (if using Docker stages)

### 3. Create Credentials
1. **Manage Jenkins** → **Manage Credentials**
2. Add GitHub credentials (username/password or SSH key)
3. Add Docker registry credentials (optional)

### 4. Create Pipeline Job
1. **New Item** → **Pipeline**
2. Name: `crud-application-pipeline`
3. Under Pipeline section:
   - Definition: `Pipeline script from SCM`
   - SCM: `Git`
   - Repository URL: Your GitHub URL
   - Credentials: Select GitHub credentials
   - Script Path: `Jenkinsfile`

### 5. Configure Webhook (Optional)
1. In GitHub repository: **Settings** → **Webhooks** → **Add webhook**
2. Payload URL: `http://your-jenkins-server/github-webhook/`
3. Content type: `application/json`
4. Events: Select `Push events`

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=UserServiceTest
mvn test -Dtest=UserControllerTest
mvn test -Dtest=UserRepositoryTest
```

### Run with Code Coverage
```bash
mvn clean verify
```

Coverage reports will be generated in `target/site/jacoco/`.

## 🌐 Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | 8080 | Server port |
| `MYSQL_HOST` | localhost | MySQL host (production) |
| `MYSQL_PORT` | 3306 | MySQL port |
| `MYSQL_DB` | cruddb | MySQL database name |
| `MYSQL_USER` | root | MySQL username |
| `MYSQL_PASSWORD` | password | MySQL password |

### Using Environment Variables

#### With Maven
```bash
export PORT=9090
mvn spring-boot:run
```

#### With Docker
```bash
docker run -p 9090:8080 -e PORT=8080 crud-application:1.0.0
```

## 🔗 GitHub Webhook Integration

### Setting Up GitHub Webhook

1. Navigate to your GitHub repository
2. Go to **Settings** → **Webhooks** → **Add webhook**
3. Configure the webhook:
   - **Payload URL**: `http://your-jenkins-server/github-webhook/`
   - **Content type**: `application/json`
   - **Secret**: (optional, configure in Jenkins)
4. Select events:
   - ☑️ **Push events**
   - ☑️ **Pull request events** (optional)
5. Click **Add webhook**

### Jenkins Configuration for Webhook

1. In Jenkins job configuration, enable:
   - **GitHub hook trigger for GITScm polling**

2. Or use Pipeline trigger:
   ```groovy
   triggers {
       githubPush()
   }
   ```

### Testing Webhook

1. Make a commit to your repository
2. Push to GitHub
3. Check Jenkins - a new build should start automatically

## 📊 H2 Database Console

Access the H2 console for development and debugging:

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:cruddb`
- Username: `sa`
- Password: (leave empty)

## 🛡️ Security Considerations

- Use environment variables for sensitive data
- Enable HTTPS in production
- Configure CORS appropriately for your domain
- Use strong database passwords
- Regularly update dependencies

## 📄 License

This project is licensed under the MIT License.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📧 Support

For support, please open an issue in the GitHub repository or contact the devops team.

---

**Built with ❤️ using Spring Boot & Jenkins**
