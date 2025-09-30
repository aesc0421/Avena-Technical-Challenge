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

- **User Management**: Registration, authentication, and password management
- **Habit Tracking**: Track exercise, sleep, hydration, and meals
- **Time-Based Periods**: Automatic session and period management for tracking habits throughout the day
- **Score Calculation**: Asynchronous calculation of habit scores using RabbitMQ
- **Scheduled Processing**: Daily automated processing of habit records at 4:00 AM (configurable)
- **RESTful API**: Complete REST API for all operations
- **Security**: Password hashing and secure authentication
- **MongoDB Integration**: Efficient NoSQL data storage with auditing

## 🛠 Technology Stack

- **Java 21**: Latest LTS version
- **Spring Boot 3.5.6**: Framework for building the application
- **Spring Data MongoDB**: Database integration
- **Spring Security**: Security and authentication
- **Spring AMQP**: RabbitMQ integration
- **RabbitMQ**: Message queue for asynchronous processing
- **MongoDB**: NoSQL database
- **Lombok**: Reduce boilerplate code
- **Maven**: Build tool and dependency management

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

1. **Java 21** or higher
   ```bash
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **MongoDB 5.0+**
   - Download from [MongoDB Official Site](https://www.mongodb.com/try/download/community)
   - Or install via Homebrew (macOS):
     ```bash
     brew tap mongodb/brew
     brew install mongodb-community@7.0
     ```

4. **RabbitMQ 3.11+**
   - Download from [RabbitMQ Official Site](https://www.rabbitmq.com/download.html)
   - Or install via Homebrew (macOS):
     ```bash
     brew install rabbitmq
     ```

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd technical_test
```

### 2. Start MongoDB

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

#### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register a new user |
| POST | `/api/users/login` | Authenticate a user |
| POST | `/api/users/change-password` | Change user password |
| POST | `/api/users/create-day` | Create a habit tracking day |

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

### Collections

#### Users Collection
```javascript
{
  "_id": ObjectId,
  "username": String,
  "firstName": String,
  "lastName": String,
  "email": String,
  "password": String (hashed),
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

#### HabitDate Collection
```javascript
{
  "_id": ObjectId,
  "userId": String,
  "date": String (YYYY-MM-DD),
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

#### HabitSession Collection
```javascript
{
  "_id": ObjectId,
  "userId": String,
  "date": String,
  "habitPeriods": Array,
  "scorePeriods": Array,
  "currentPeriodIndex": Number,
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

#### HabitRecord Collection
```javascript
{
  "_id": ObjectId,
  "userId": String,
  "date": String,
  "nutritionMeals": {
    "breakfast": Boolean,
    "snackOne": Boolean,
    "meal": Boolean,
    "snackTwo": Boolean,
    "dinner": Boolean
  },
  "exerciseMinutes": Number,
  "sleepMinutes": Number,
  "hydrationMl": Number,
  "notes": String,
  "scored": Boolean,
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

#### HabitScore Collection
```javascript
{
  "_id": ObjectId,
  "userId": String,
  "recordId": String,
  "date": String,
  "nutritionScore": Number,
  "exerciseScore": Number,
  "sleepScore": Number,
  "hydrationScore": Number,
  "overallScore": Number,
  "calculatedAt": ISODate,
  "createdAt": ISODate,
  "updatedAt": ISODate
}
```

## 🐛 Troubleshooting

### MongoDB Connection Issues

**Problem**: `MongoSocketOpenException: Connection refused`

**Solution**:
1. Verify MongoDB is running:
   ```bash
   brew services list | grep mongodb
   ```
2. Start MongoDB if not running:
   ```bash
   brew services start mongodb-community@7.0
   ```
3. Check MongoDB logs:
   ```bash
   tail -f /usr/local/var/log/mongodb/mongo.log
   ```

### RabbitMQ Connection Issues

**Problem**: `AmqpConnectException: Connection refused`

**Solution**:
1. Verify RabbitMQ is running:
   ```bash
   brew services list | grep rabbitmq
   ```
2. Start RabbitMQ if not running:
   ```bash
   brew services start rabbitmq
   ```
3. Check RabbitMQ status:
   ```bash
   rabbitmqctl status
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

## 🔒 Security Notes

- Passwords are hashed using BCrypt before storage
- Never commit sensitive credentials to version control
- Update default MongoDB and RabbitMQ passwords in production
- Consider implementing JWT tokens for stateless authentication
- Enable HTTPS in production environments

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