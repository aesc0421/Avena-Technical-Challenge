# Habit Tracking System with RabbitMQ

A comprehensive Spring Boot application for tracking daily health habits including exercise, sleep, hydration, and nutrition. The system uses MongoDB for data persistence and RabbitMQ for asynchronous score calculations.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Architecture](#architecture)
- [RabbitMQ Integration](#rabbitmq-integration)
- [Database Schema](#database-schema)
- [Troubleshooting](#troubleshooting)
- [License](#license)

## ✨ Features

### Currently Active
- ✅ **User Management**: Registration, authentication, and password management
- ✅ **Habit Date Tracking**: Create and manage daily habit tracking dates
- ✅ **RESTful API**: REST API for user and date management
- ✅ **Security**: Password hashing with BCrypt and secure authentication
- ✅ **MongoDB Integration**: Efficient NoSQL data storage with auditing
- ✅ **Docker Support**: Easy deployment with Docker containers

### Planned Features
- 🔜 **Habit Tracking**: Track exercise, sleep, hydration, and meals
- 🔜 **Time-Based Periods**: Automatic session and period management
- 🔜 **Score Calculation**: Asynchronous calculation using RabbitMQ
- 🔜 **Scheduled Processing**: Daily automated processing of habit records
- 🔜 **Analytics & Reporting**: Historical data analysis and insights

## 🛠 Technology Stack

### Core Technologies
- **Java 21**: Latest LTS version
- **Spring Boot 3.5.6**: Framework for building the application
- **Maven**: Build tool and dependency management

### Active Dependencies
- **Spring Data MongoDB**: Database integration and repository layer
- **Spring Security**: Security framework and authentication
- **BCrypt**: Password hashing algorithm (via Spring Security)
- **MongoDB 7.0**: NoSQL database (Docker container)
- **RabbitMQ 3.11**: Message queue for asynchronous processing (Docker container)
- **Lombok**: Reduce boilerplate code

### Planned Integrations
- **Spring AMQP**: RabbitMQ message processing (configured, will be used for scoring)
- **Scheduled Tasks**: Cron-based job processing

## 📦 Prerequisites

### Option 1: Using Docker (Recommended)

This project uses **Docker** for MongoDB and RabbitMQ services.

1. **Docker Desktop** or **Docker Engine**
   ```bash
   docker --version
   ```
   - Download from [Docker Official Site](https://www.docker.com/products/docker-desktop)

2. **Java 21** (to run the Spring Boot application)
   ```bash
   java -version
   ```

3. **Maven 3.6+**
   ```bash
   mvn -version
   ```

### Option 2: Local Installation (Without Docker)

1. **Java 21** or higher
2. **Maven 3.6+**
3. **MongoDB 5.0+** (local installation)
4. **RabbitMQ 3.11+** (local installation)

## 🚀 Installation & Setup

### 🐳 Quick Start with Docker Compose (Recommended)

**The easiest way to run everything with a single command:**

```bash
# Start all services (MongoDB + RabbitMQ + Spring Boot App)
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down
```

That's it! Your application will be running at **http://localhost:8080** 🚀

**What this does:**
- ✅ Starts MongoDB on port 27017
- ✅ Starts RabbitMQ on ports 5672 and 15672 (management UI)
- ✅ Builds and starts your Spring Boot application on port 8080
- ✅ Creates a network for the containers to communicate
- ✅ Sets up persistent volumes for data

---

### 🐳 Alternative: Use Existing Docker Containers

If you already have Docker containers running:

```bash
# 1. Start MongoDB container
docker start avena

# 2. Start RabbitMQ container
docker start rabbitmq

# 3. Verify containers are running
docker ps

# 4. Build and run the application locally
mvn clean package
java -jar target/technical_test-0.0.1-SNAPSHOT.jar
```

---

### Docker Setup (Detailed Instructions)

#### 1. Clone the Repository

```bash
git clone <repository-url>
cd technical_test
```

#### 2. Start Docker Containers

You have two Docker images that need to be running: MongoDB and RabbitMQ.

**If containers already exist (recommended):**
```bash
docker start avena rabbitmq
```

**If containers don't exist yet, create them:**

**Start MongoDB Container:**
```bash
# Run MongoDB container (named 'avena')
docker run -d \
  --name avena \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=passwd \
  -e MONGO_INITDB_DATABASE=habits \
  mongo:7
```

**Start RabbitMQ Container:**
```bash
# Run RabbitMQ container with management plugin
docker run -d \
  --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3.11-management
```

**Verify Containers are Running:**
```bash
docker ps
```

You should see both `avena` (MongoDB) and `rabbitmq` containers running.

**Check Container Logs:**
```bash
# MongoDB logs
docker logs avena

# RabbitMQ logs
docker logs rabbitmq
```

#### 3. Access Services

Once containers are running:
- **MongoDB**: `localhost:27017` (username: `admin`, password: `passwd`)
- **RabbitMQ AMQP**: `localhost:5672`
- **RabbitMQ Management UI**: http://localhost:15672 (username: `guest`, password: `guest`)

#### 4. Initialize MongoDB Database

Connect to MongoDB and create the database:

```bash
# Connect to MongoDB container
docker exec -it avena mongosh -u admin -p passwd --authenticationDatabase admin
```

Then run these commands in the MongoDB shell:

```javascript
use habits
db.createCollection("users")
db.createCollection("habitDates")

// Create indexes for better performance
db.users.createIndex({ "email": 1 }, { unique: true })
db.users.createIndex({ "username": 1 }, { unique: true })
db.habitDates.createIndex({ "userId": 1, "date": 1 }, { unique: true })

exit
```

#### 5. Build and Run the Spring Boot Application

```bash
# Build the project
mvn clean package

# Run the application
java -jar target/technical_test-0.0.1-SNAPSHOT.jar
```

The application will connect to the Docker containers and start on **http://localhost:8080**

#### 6. Managing Docker Containers

**Stop containers:**
```bash
docker stop avena rabbitmq
```

**Start containers (if already created):**
```bash
docker start avena rabbitmq
```

**Remove containers (WARNING: deletes all data):**
```bash
docker rm -f avena rabbitmq
```

**View container status:**
```bash
docker ps -a
```

---

### Local Setup (Without Docker)

<details>
<summary>Click to expand local installation instructions</summary>

#### 1. Clone the Repository

```bash
git clone <repository-url>
cd technical_test
```

#### 2. Start MongoDB

**macOS (Homebrew):**
```bash
brew services start mongodb-community@7.0
```

**Manual Start:**
```bash
mongod --config /usr/local/etc/mongod.conf
```

**Verify MongoDB is running:**
```bash
mongosh
```

### 3. Create MongoDB Database and User

```bash
mongosh
```

```javascript
use admin
db.createUser({
  user: "admin",
  pwd: "passwd",
  roles: [ { role: "readWriteAnyDatabase", db: "admin" } ]
})

use habits
db.createCollection("users")
```

### 4. Start RabbitMQ

**macOS (Homebrew):**
```bash
brew services start rabbitmq
```

**Manual Start:**
```bash
rabbitmq-server
```

**Verify RabbitMQ is running:**
- Open RabbitMQ Management UI: http://localhost:15672
- Default credentials: `guest` / `guest`

### 5. Build the Project

```bash
mvn clean install
```

</details>

---

## ⚙️ Configuration

The application configuration is located in `src/main/resources/application.properties`:

### MongoDB Configuration

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=habits
spring.data.mongodb.username=admin
spring.data.mongodb.password=passwd
spring.data.mongodb.authentication-database=admin
```

### RabbitMQ Configuration

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
spring.rabbitmq.virtual-host=/
```

### Queue Configuration

```properties
habit.queue.main=habit.main.queue
habit.queue.individual=habit.individual.queue
habit.exchange=habit.exchange
```

### Scheduler Configuration

```properties
# For testing: Run every minute
spring.scheduling.cron.habit-score-scheduler=0 * * * * *

# For production: Run at 4:00 AM daily
# spring.scheduling.cron.habit-score-scheduler=0 0 4 * * *

spring.scheduling.zone=America/Mexico_City
```

## 🏃 Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using Maven Wrapper

```bash
./mvnw spring-boot:run
```

### Using JAR

```bash
mvn clean package
java -jar target/technical_test-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

### Verify Application is Running

```bash
curl http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "password": "test123"
  }'
```

## 📚 API Documentation

Complete API documentation is available in [API_DOCUMENTATION.md](API_DOCUMENTATION.md).

### Quick Start - API Endpoints

#### Currently Available Endpoints

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| POST | `/api/users/register` | Register a new user | ✅ Active |
| POST | `/api/users/login` | Authenticate a user | ✅ Active |
| POST | `/api/users/change-password` | Change user password | ✅ Active |
| POST | `/api/users/create-day` | Create a habit tracking day | ✅ Active |

**Note**: Additional endpoints for habit tracking, sessions, and scoring are planned for future releases. See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for details.

#### Example: Register a User

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

#### Example: Login

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

#### Example: Create a Habit Day

```bash
curl -X POST http://localhost:8080/api/users/create-day \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "USER_ID_HERE",
    "date": "2025-09-30",
    "notes": "Starting my habit tracking journey"
  }'
```

## 🏗 Architecture

The application follows a layered architecture:

```
┌─────────────────────────────────────────┐
│         Controllers Layer               │
│  (REST API endpoints - UserController)  │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│          Service Layer                  │
│  (Business Logic - UserService, etc.)   │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│        Repository Layer                 │
│   (Data Access - Spring Data MongoDB)   │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│         MongoDB Database                │
└─────────────────────────────────────────┘

        Async Processing
┌─────────────────────────────────────────┐
│      Scheduler (Cron Jobs)              │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│      RabbitMQ Queue Service             │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│   Message Processors & Calculators      │
└─────────────────────────────────────────┘
```

### Key Components

- **Controllers**: Handle HTTP requests and responses
- **Services**: Implement business logic
- **Repositories**: Data access layer using Spring Data MongoDB
- **Models**: Entity classes representing data structures
- **DTOs**: Data Transfer Objects for message queues
- **Config**: Configuration classes for Security, RabbitMQ, etc.
- **Scheduler**: Scheduled tasks for automated processing

## 🔄 RabbitMQ Integration

The system uses RabbitMQ for asynchronous habit score calculations. For detailed architecture, see [RABBITMQ_ARCHITECTURE.md](RABBITMQ_ARCHITECTURE.md).

### Queue Architecture

1. **Main Queue** (`habit.main.queue`): Daily batch processing
2. **Individual Queue** (`habit.individual.queue`): Per-record score calculation

### Processing Flow

```
Scheduler (4:00 AM) → Enqueue Daily Task → Process Records → 
Enqueue Individual Tasks → Calculate Scores → Save to DB
```

### Monitoring RabbitMQ

Access the RabbitMQ Management Console:
- URL: http://localhost:15672
- Username: `guest`
- Password: `guest`

Here you can:
- Monitor queue lengths
- View message rates
- Check consumer status
- Manage exchanges and bindings

## 🗄 Database Schema

### Active Collections

Currently, the application uses two MongoDB collections:

#### Users Collection
```javascript
{
  "_id": ObjectId,
  "username": String,
  "firstName": String,
  "lastName": String,
  "email": String,
  "password": String (hashed with BCrypt),
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

**Indexes:**
- `email` (unique): Fast user lookup by email for authentication
- `username` (unique): Ensures username uniqueness

#### HabitDates Collection
```javascript
{
  "_id": ObjectId,
  "userId": String,
  "date": String (YYYY-MM-DD format),
  "notes": String (optional),
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

**Indexes:**
- `userId + date` (compound, unique): Prevents duplicate dates per user and enables fast lookups

### Planned Collections (Future)

The following collections will be added when habit tracking features are implemented:

- **HabitRecords**: Store detailed habit data (exercise, sleep, hydration, nutrition)
- **HabitSessions**: Track habit periods throughout the day
- **HabitScores**: Calculated scores for habit performance

## 🐛 Troubleshooting

### Docker Container Issues

**Problem**: Docker containers not running

**Solution**:
1. Check if containers exist and their status:
   ```bash
   docker ps -a
   ```
2. Start stopped containers:
   ```bash
   docker start avena rabbitmq
   ```
3. Check container logs for errors:
   ```bash
   docker logs avena
   docker logs rabbitmq
   ```
4. If containers are stuck or errored, restart them:
   ```bash
   docker restart avena rabbitmq
   ```

**Problem**: Containers don't exist (error: "No such container")

**Solution**:
Create the containers using the docker run commands from the setup section:
```bash
# Create MongoDB container
docker run -d --name avena -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=passwd \
  mongo:7

# Create RabbitMQ container  
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 \
  rabbitmq:3.11-management
```

**Problem**: Port already in use (when starting Docker containers)

**Solution**:
1. Check what's using the ports:
   ```bash
   lsof -i :27017  # MongoDB
   lsof -i :5672   # RabbitMQ
   lsof -i :15672  # RabbitMQ Management
   ```
2. Stop conflicting containers or services:
   ```bash
   # If another container is using the port
   docker ps
   docker stop <container-name>
   
   # If local MongoDB/RabbitMQ is running
   brew services stop mongodb-community
   brew services stop rabbitmq
   ```

### MongoDB Connection Issues (Docker)

**Problem**: `MongoSocketOpenException: Connection refused`

**Solution**:
1. Verify MongoDB container is running:
   ```bash
   docker ps | grep avena
   ```
2. Start MongoDB container if not running:
   ```bash
   docker start avena
   ```
3. Check MongoDB container logs:
   ```bash
   docker logs avena
   ```
4. Verify connection from inside container:
   ```bash
   docker exec -it avena mongosh -u admin -p passwd --authenticationDatabase admin
   ```

### RabbitMQ Connection Issues (Docker)

**Problem**: `AmqpConnectException: Connection refused`

**Solution**:
1. Verify RabbitMQ container is running:
   ```bash
   docker ps | grep rabbitmq
   ```
2. Start RabbitMQ container if not running:
   ```bash
   docker start rabbitmq
   ```
3. Check RabbitMQ container logs:
   ```bash
   docker logs rabbitmq
   ```
4. Test RabbitMQ Management UI:
   - Open: http://localhost:15672
   - Login with: guest / guest
5. Verify connection from inside container:
   ```bash
   docker exec -it rabbitmq rabbitmqctl status
   ```

### Port Already in Use

**Problem**: `Port 8080 already in use`

**Solution**:
1. Find process using port 8080:
   ```bash
   lsof -i :8080
   ```
2. Kill the process:
   ```bash
   kill -9 <PID>
   ```
3. Or change the port in `application.properties`:
   ```properties
   server.port=8081
   ```

### Build Issues

**Problem**: Maven build fails

**Solution**:
1. Clean Maven cache:
   ```bash
   mvn clean
   ```
2. Update dependencies:
   ```bash
   mvn clean install -U
   ```
3. Verify Java version:
   ```bash
   java -version  # Should be Java 21+
   ```

### Application Won't Start

**Problem**: Application fails to start

**Solutions**:
1. Check all services are running (MongoDB, RabbitMQ)
2. Verify configuration in `application.properties`
3. Check application logs for specific error messages
4. Ensure ports 8080, 5672, and 27017 are available

## 📊 Monitoring and Logs

### Application Logs

The application uses Spring Boot's default logging:
- Log level: INFO
- MongoDB operations: DEBUG
- Location: Console output

### Enable Debug Logging

Add to `application.properties`:
```properties
logging.level.com.avenaio.technical_test=DEBUG
logging.level.org.springframework.amqp=DEBUG
```

### Health Checks

Monitor application health:
```bash
curl http://localhost:8080/actuator/health
```

## 🔒 Security Features

### Currently Implemented

- ✅ **BCrypt Password Hashing**: All passwords are hashed using BCrypt with salt before storage
  - Industry-standard hashing algorithm
  - Adaptive cost factor for future-proofing
  - One-way encryption - passwords cannot be decrypted
  
- ✅ **Spring Security**: Authentication and authorization framework
  - Secure password validation
  - Protection against common vulnerabilities

- ✅ **Input Validation**: Request validation using Spring Validation
  - Email format validation
  - Required field checks
  - Unique constraint enforcement

### Security Best Practices

- 🔐 Passwords are never returned in API responses
- 🔐 Never commit sensitive credentials to version control
- 🔐 Update default MongoDB and RabbitMQ passwords in production
- 🔐 Use strong passwords (minimum 8+ characters recommended)

### Production Recommendations

- Consider implementing JWT tokens for stateless authentication
- Enable HTTPS/SSL for all endpoints
- Implement rate limiting to prevent brute-force attacks
- Set up API key management for service-to-service communication
- Enable audit logging for security events

## 🚀 Deployment

### Production Checklist

- [ ] Update MongoDB credentials
- [ ] Update RabbitMQ credentials
- [ ] Change scheduler to production cron (4:00 AM)
- [ ] Enable HTTPS/SSL
- [ ] Configure firewall rules
- [ ] Set up monitoring and alerts
- [ ] Configure backup strategy
- [ ] Review and update security settings
- [ ] Set appropriate logging levels
- [ ] Configure resource limits (memory, CPU)

### Environment Variables

For production, use environment variables instead of hardcoded values:

```bash
export MONGODB_HOST=your-mongo-host
export MONGODB_PORT=27017
export MONGODB_DATABASE=habits
export MONGODB_USERNAME=your-username
export MONGODB_PASSWORD=your-password
export RABBITMQ_HOST=your-rabbitmq-host
export RABBITMQ_PORT=5672
```

## 📝 License

This project is developed as a technical test for Avena.

## 👥 Support

For issues, questions, or contributions:
1. Check the [API Documentation](API_DOCUMENTATION.md)
2. Review the [RabbitMQ Architecture](RABBITMQ_ARCHITECTURE.md)
3. Check existing issues in the repository
4. Create a new issue with detailed information

## 🔗 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [MongoDB Documentation](https://docs.mongodb.com/)
- [RabbitMQ Documentation](https://www.rabbitmq.com/documentation.html)
- [Spring AMQP Documentation](https://spring.io/projects/spring-amqp)
- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)

---

**Built with ❤️ for Avena Technical Test**