# API Documentation - Habit Tracking System

This documentation covers all available endpoints in the Habit Tracking System with RabbitMQ integration.

## Base URL
```
http://localhost:8080
```

## Authentication
Currently, the API uses basic authentication. Include user credentials where required.

---

## 📋 Table of Contents

1. [User Management Endpoints](#user-management-endpoints)
2. [Habit Date Management](#habit-date-management)
3. [Future Endpoints (Currently Disabled)](#future-endpoints-currently-disabled)
4. [Common Response Patterns](#common-response-patterns)
5. [RabbitMQ Integration](#rabbitmq-integration)
6. [Testing the API](#testing-the-api)

---

## 👤 User Management Endpoints

### 1. Register User
**POST** `/api/users/register`

Register a new user in the system.

**Request Body:**
```json
{
  "username": "john_doe",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response (Success - 200):**
```json
{
  "message": "User registered successfully",
  "user": {
    "id": "60f4b3b5b8f3a2001f5e4a1b",
    "username": "john_doe",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "createdAt": "2025-09-30T10:30:00Z",
    "updatedAt": "2025-09-30T10:30:00Z"
  }
}
```

**Response (Error - 400):**
```json
{
  "error": "Email already exists"
}
```

**Validation Rules:**
- `username`: Required, must be unique
- `firstName`: Required
- `lastName`: Required
- `email`: Required, must be valid email format, must be unique
- `password`: Required, minimum 6 characters recommended

---

### 2. Login User
**POST** `/api/users/login`

Authenticate a user with email and password.

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response (Success - 200):**
```json
{
  "message": "Login successful",
  "user": {
    "id": "60f4b3b5b8f3a2001f5e4a1b",
    "username": "john_doe",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "createdAt": "2025-09-30T10:30:00Z",
    "updatedAt": "2025-09-30T10:30:00Z"
  }
}
```

**Response (Error - 400):**
```json
{
  "error": "Invalid credentials"
}
```

**Security Notes:**
- Passwords are hashed using BCrypt
- Password is never returned in responses
- Failed login attempts are logged

---

### 3. Change Password
**POST** `/api/users/change-password`

Change user's password.

**Request Body:**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "oldPassword": "currentPassword123",
  "newPassword": "newSecurePassword456"
}
```

**Response (Success - 200):**
```json
{
  "message": "Password changed successfully"
}
```

**Response (Error - 400):**
```json
{
  "error": "Invalid old password"
}
```

**Additional Error Cases:**
```json
{
  "error": "User ID is required"
}
```

```json
{
  "error": "User not found"
}
```

**Validation Rules:**
- `userId`: Required, must be valid MongoDB ObjectId
- `oldPassword`: Required, must match current password
- `newPassword`: Required, should be different from old password

---

## 📅 Habit Date Management

### 4. Create Habit Day
**POST** `/api/users/create-day`

Create a new habit tracking day for a user.

**Request Body:**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "date": "2025-09-30",
  "notes": "Starting my habit tracking journey"
}
```

**Alternative Request (Minimal):**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "date": "2025-09-30"
}
```

**Response (Success - 200):**
```json
{
  "message": "Day created successfully"
}
```

**Response (Error - 400):**
```json
{
  "error": "Date already exists for this user"
}
```

**Field Descriptions:**
- `userId`: String - User identifier (required)
- `date`: String - Date in YYYY-MM-DD format (required)
- `notes`: String - Optional notes about the day

---

## 🔮 Future Endpoints (Currently Disabled)

The following endpoints are planned for future releases and are currently disabled in the codebase. They are documented here for reference and will be activated in upcoming versions.

### Habit Management Endpoints (Coming Soon)

#### Update Exercise
**POST** `/api/habits/update-exercise` _(Disabled)_

Update exercise minutes for current period.

#### Update Sleep
**POST** `/api/habits/update-sleep` _(Disabled)_

Update sleep minutes for current period.

#### Update Hydration
**POST** `/api/habits/update-hydration` _(Disabled)_

Update hydration in milliliters for current period.

#### Register Meal
**POST** `/api/habits/meals/register` _(Disabled)_

Register a meal for the current period.

#### Create Daily Record
**POST** `/api/habits/create-daily-record` _(Disabled)_

Create a complete daily habit record with detailed meal tracking.

### Session-Based Endpoints (Coming Soon)

#### Get User Session by Date
**GET** `/api/habits/sessions/user/{userId}/date/{date}` _(Disabled)_

Get habit session with all periods for a specific date.

#### Get All User Sessions
**GET** `/api/habits/sessions/user/{userId}` _(Disabled)_

Get all sessions for a user.

#### Create New Period
**POST** `/api/habits/sessions/new-period` _(Disabled)_

Manually create a new period (for testing time lapse).

### Score Retrieval Endpoints (Coming Soon)

#### Get Latest Scores
**GET** `/api/habits/scores/user/{userId}/latest` _(Disabled)_

Get the latest calculated scores for a user.

#### Get Paginated Scores
**GET** `/api/habits/scores/user/{userId}` _(Disabled)_

Get paginated scores for a user with period details.

#### Get Score History
**GET** `/api/habits/scores/user/{userId}/history` _(Disabled)_

Get scores across multiple dates with pagination.

---

## 📝 Common Response Patterns

### Success Response Structure
```json
{
  "message": "Operation successful",
  "data": { /* relevant data */ }
}
```

### Error Response Structure
```json
{
  "error": "Description of what went wrong"
}
```

### HTTP Status Codes

| Code | Description | When Used |
|------|-------------|-----------|
| 200 | OK | Successful request |
| 400 | Bad Request | Invalid input, validation errors |
| 401 | Unauthorized | Authentication required |
| 404 | Not Found | Resource not found |
| 500 | Internal Server Error | Server-side error |

---

## 🔄 RabbitMQ Integration

The system uses RabbitMQ for asynchronous habit score calculations. For detailed architecture, see [RABBITMQ_ARCHITECTURE.md](RABBITMQ_ARCHITECTURE.md).

### Queue Architecture

The application implements a two-tier queue system:

1. **Main Queue** (`habit.main.queue`)
   - Purpose: Daily batch processing
   - Triggered by: Scheduler (configurable, default 4:00 AM)
   - Processes: All habit records from previous day
   - Consumers: 3-10 concurrent consumers
   - Message TTL: 5 minutes

2. **Individual Queue** (`habit.individual.queue`)
   - Purpose: Per-record score calculation
   - Triggered by: Main queue processor
   - Processes: Individual habit records
   - Consumers: 3-10 concurrent consumers
   - Message TTL: 10 minutes

### Exchange Configuration

- **Exchange Name**: `habit.exchange`
- **Type**: Topic
- **Durability**: Yes (survives broker restart)
- **Bindings**:
  - `habit.main` → `habit.main.queue`
  - `habit.individual` → `habit.individual.queue`

### Processing Flow

```
┌─────────────────────────────────────────────┐
│  1. Scheduler (Cron Job)                    │
│     - Triggers daily at 4:00 AM             │
│     - Sends message to main queue           │
└─────────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────┐
│  2. Main Queue Processor                    │
│     - Retrieves all unscored records        │
│     - Groups by user and date               │
│     - Enqueues individual tasks             │
└─────────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────┐
│  3. Individual Queue Processors             │
│     - Calculate nutrition score (0-100)     │
│     - Calculate exercise score (0-100)      │
│     - Calculate sleep score (0-100)         │
│     - Calculate hydration score (0-100)     │
│     - Calculate overall score (average)     │
└─────────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────┐
│  4. Score Persistence                       │
│     - Save scores to MongoDB                │
│     - Mark record as scored                 │
│     - Update habit session                  │
└─────────────────────────────────────────────┘
```

### Score Calculation Details

**Nutrition Score:**
- Based on number of meals consumed
- 5 meals total: breakfast, snack, meal, snack, dinner
- Score = (meals consumed / 5) × 100

**Exercise Score:**
- Target: 60 minutes daily
- Score = min(exercise minutes / 60 × 100, 100)

**Sleep Score:**
- Target: 480 minutes (8 hours)
- Score = min(sleep minutes / 480 × 100, 100)

**Hydration Score:**
- Target: 2000 ml
- Score = min(hydration ml / 2000 × 100, 100)

**Overall Score:**
- Average of all four category scores

### Message Formats

**Daily Processing Message:**
```json
{
  "date": "2025-09-30",
  "scheduledTime": "2025-09-30T04:00:00Z",
  "reason": "Daily scheduled processing"
}
```

**Individual Task Message:**
```json
{
  "recordId": "60f4b3b5b8f3a2001f5e4a1b",
  "userId": "60f4b3b5b8f3a2001f5e4a1a",
  "date": "2025-09-30"
}
```

### Monitoring RabbitMQ

Access the RabbitMQ Management Console at http://localhost:15672

**Default Credentials:**
- Username: `guest`
- Password: `guest`

**Available Metrics:**
- Queue lengths and message rates
- Consumer status and performance
- Exchange bindings and routing
- Connection and channel details
- Message acknowledgment rates

### Configuration

Queue settings can be modified in `application.properties`:

```properties
# RabbitMQ Connection
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

# Queue Names
habit.queue.main=habit.main.queue
habit.queue.individual=habit.individual.queue
habit.exchange=habit.exchange

# Scheduler Configuration
spring.scheduling.cron.habit-score-scheduler=0 0 4 * * *
spring.scheduling.zone=America/Mexico_City
```

---

## 🛠️ Testing the API

### Using cURL

#### 1. Register a User

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "password": "testpass123"
  }'
```

**Expected Response:**
```json
{
  "message": "User registered successfully",
  "user": {
    "id": "66faa5e4c8f3a2001f5e4a1b",
    "username": "testuser",
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com"
  }
}
```

---

#### 2. Login

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "testpass123"
  }'
```

**Expected Response:**
```json
{
  "message": "Login successful",
  "user": {
    "id": "66faa5e4c8f3a2001f5e4a1b",
    "username": "testuser",
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com"
  }
}
```

---

#### 3. Change Password

```bash
curl -X POST http://localhost:8080/api/users/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "66faa5e4c8f3a2001f5e4a1b",
    "oldPassword": "testpass123",
    "newPassword": "newpass456"
  }'
```

**Expected Response:**
```json
{
  "message": "Password changed successfully"
}
```

---

#### 4. Create Habit Day

```bash
curl -X POST http://localhost:8080/api/users/create-day \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "66faa5e4c8f3a2001f5e4a1b",
    "date": "2025-09-30",
    "notes": "My first day tracking habits"
  }'
```

**Expected Response:**
```json
{
  "message": "Day created successfully"
}
```

---

### Using Postman

1. **Import Collection**:
   - Create a new collection named "Habit Tracking API"
   - Set base URL variable: `{{baseUrl}}` = `http://localhost:8080`

2. **Add Requests**:
   - Create requests for each endpoint
   - Use environment variables for user IDs and dates

3. **Test Scenarios**:
   - Register multiple users
   - Test login with valid/invalid credentials
   - Test password changes
   - Create habit days for different dates

### Using HTTPie

```bash
# Install HTTPie
brew install httpie

# Register user
http POST localhost:8080/api/users/register \
  username=testuser \
  firstName=Test \
  lastName=User \
  email=test@example.com \
  password=testpass123

# Login
http POST localhost:8080/api/users/login \
  email=test@example.com \
  password=testpass123

# Create day
http POST localhost:8080/api/users/create-day \
  userId=66faa5e4c8f3a2001f5e4a1b \
  date=2025-09-30 \
  notes="First day"
```

---

## 🧪 Testing Workflow

### Complete User Flow

1. **Register a new user**
2. **Login to get user ID**
3. **Create a habit day** for today
4. _(Future)_ Update habits throughout the day
5. _(Future)_ View scores after scheduled processing

### Test Data Cleanup

To reset test data:

```bash
# Connect to MongoDB
mongosh

# Switch to habits database
use habits

# Clear collections
db.users.deleteMany({email: /test@/})
db.habitDates.deleteMany({})
db.habitRecords.deleteMany({})
db.habitScores.deleteMany({})
db.habitSessions.deleteMany({})
```

---

## 📚 Additional Notes

### Data Formats

- **Timestamps**: ISO 8601 format (UTC)
  - Example: `2025-09-30T10:30:00Z`
- **Dates**: YYYY-MM-DD format
  - Example: `2025-09-30`
- **User IDs**: MongoDB ObjectId as string
  - Example: `60f4b3b5b8f3a2001f5e4a1b`
- **Scores**: Integer 0-100 scale

### Best Practices

1. **User Registration**:
   - Use strong passwords (minimum 8 characters)
   - Unique email addresses required
   - Validate email format on client side

2. **Authentication**:
   - Store user ID after successful login
   - Don't store passwords on client
   - Implement token-based auth in production

3. **Date Handling**:
   - Always use YYYY-MM-DD format
   - Consider timezone differences
   - Use current date for real-time tracking

4. **Error Handling**:
   - Check response status codes
   - Display user-friendly error messages
   - Log errors for debugging

### Rate Limiting

Currently, there are no rate limits implemented. For production:
- Implement rate limiting per IP/user
- Set reasonable request limits
- Add throttling for queue operations

### API Versioning

Current version: v1 (implicit in `/api` prefix)

Future versions will use explicit versioning:
- `/api/v1/users/register`
- `/api/v2/users/register`

### Support for Different Date Formats

The API currently only supports YYYY-MM-DD format. When integrating:
- Always format dates before sending
- Parse response dates consistently
- Handle timezone conversions appropriately

---

## 🔐 Security Considerations

1. **Password Security**:
   - BCrypt hashing with salt
   - Minimum password requirements
   - Password never returned in responses

2. **Input Validation**:
   - All inputs are validated
   - SQL/NoSQL injection prevention
   - XSS protection

3. **Future Enhancements**:
   - JWT token authentication
   - OAuth2 integration
   - API key management
   - Role-based access control (RBAC)

---

## 📊 Database Indexes

Recommended indexes for optimal performance:

```javascript
// Users collection
db.users.createIndex({ "email": 1 }, { unique: true })
db.users.createIndex({ "username": 1 }, { unique: true })

// HabitDates collection
db.habitDates.createIndex({ "userId": 1, "date": 1 }, { unique: true })

// HabitRecords collection
db.habitRecords.createIndex({ "userId": 1, "date": 1 })
db.habitRecords.createIndex({ "scored": 1, "date": 1 })

// HabitScores collection
db.habitScores.createIndex({ "userId": 1, "date": -1 })
db.habitScores.createIndex({ "recordId": 1 })
```

---

## 🚀 Migration Guide

When updating from earlier versions:

### Version 0.0.1 (Current)
- Initial release
- Basic user management
- Habit day creation
- RabbitMQ queue setup

### Upcoming Features
- Habit tracking endpoints activation
- Session management
- Real-time score calculations
- Historical data analytics

---

## 📞 Support & Feedback

For questions, issues, or suggestions:
1. Review this documentation
2. Check [RABBITMQ_ARCHITECTURE.md](RABBITMQ_ARCHITECTURE.md)
3. Review [README.md](README.md) for setup instructions
4. Create an issue with detailed information

---

**Last Updated**: September 30, 2025  
**Version**: 0.0.1-SNAPSHOT  
**Maintained by**: Avena Technical Test Team