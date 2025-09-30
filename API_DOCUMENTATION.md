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
2. [Habit Management Endpoints](#habit-management-endpoints)
3. [Session-Based Endpoints](#session-based-endpoints)
4. [Score Retrieval Endpoints](#score-retrieval-endpoints)

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
    "createdAt": "2023-07-19T10:30:00Z"
  }
}
```

**Response (Error - 400):**
```json
{
  "error": "Email already exists"
}
```

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
    "email": "john@example.com"
  }
}
```

**Response (Error - 400):**
```json
{
  "error": "Invalid credentials"
}
```

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

---

## 🏃‍♀️ Habit Management Endpoints

### 1. Update Exercise
**POST** `/api/habits/update-exercise`

Update exercise minutes for current period.

**Request Body:**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "minutes": "45"
}
```

**Response (Success - 200):**
```json
{
  "message": "Exercise updated successfully in current period",
  "minutes": 45,
  "sessionId": "session_123",
  "periodId": "period_456",
  "currentPeriod": {
    "periodId": "period_456",
    "exerciseMinutes": 45,
    "startTime": "2023-07-19T08:00:00Z",
    "isActive": true
  },
  "totalPeriods": 3
}
```

---

### 2. Update Sleep
**POST** `/api/habits/update-sleep`

Update sleep minutes for current period.

**Request Body:**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "minutes": "480"
}
```

**Response (Success - 200):**
```json
{
  "message": "Sleep updated successfully in current period",
  "minutes": 480,
  "sessionId": "session_123",
  "periodId": "period_456",
  "currentPeriod": {
    "periodId": "period_456",
    "sleepMinutes": 480,
    "startTime": "2023-07-19T08:00:00Z",
    "isActive": true
  },
  "totalPeriods": 3
}
```

---

### 3. Update Hydration
**POST** `/api/habits/update-hydration`

Update hydration in milliliters for current period.

**Request Body:**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "milliliters": "2000"
}
```

**Response (Success - 200):**
```json
{
  "message": "Hydration updated successfully in current period",
  "milliliters": 2000,
  "sessionId": "session_123",
  "periodId": "period_456",
  "currentPeriod": {
    "periodId": "period_456",
    "hydrationMl": 2000,
    "startTime": "2023-07-19T08:00:00Z",
    "isActive": true
  },
  "totalPeriods": 3
}
```

---

### 4. Register Meal
**POST** `/api/habits/meals/register`

Register a meal for the current period.

**Request Body:**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "mealType": "breakfast"
}
```

**Valid meal types:** `breakfast`, `snackOne`, `meal`, `snackTwo`, `dinner`

**Response (Success - 200):**
```json
{
  "message": "Meal registered successfully in current period",
  "mealType": "breakfast",
  "sessionId": "session_123",
  "periodId": "period_456",
  "currentPeriod": {
    "periodId": "period_456",
    "nutritionMeals": {
      "breakfast": true,
      "snackOne": false,
      "meal": false,
      "snackTwo": false,
      "dinner": false
    }
  },
  "totalPeriods": 3
}
```

---

### 5. Create Daily Record
**POST** `/api/habits/create-daily-record`

Create a complete daily habit record with detailed meal tracking.

**Request Body:**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "notes": "Had a productive day",
  "meals": {
    "breakfast": true,
    "snackOne": false,
    "meal": true,
    "snackTwo": true,
    "dinner": true
  },
  "exerciseMinutes": 60,
  "sleepMinutes": 480,
  "hydrationMl": 2500
}
```

**Alternative Request Body (String format also supported):**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "notes": "Had a productive day",
  "meals": {
    "breakfast": true,
    "snackOne": false,
    "meal": true,
    "snackTwo": true,
    "dinner": true
  },
  "exerciseMinutes": "60",
  "sleepMinutes": "480",
  "hydrationMl": "2500"
}
```

**Field Descriptions:**
- `userId`: String - User identifier (required)
- `notes`: String - Optional notes about the day
- `meals`: Object - Individual meal tracking (optional)
- `exerciseMinutes`: Number or String - Minutes of exercise (optional)
- `sleepMinutes`: Number or String - Minutes of sleep (optional)
- `hydrationMl`: Number or String - Milliliters of water consumed (optional)

**Meals Object Properties:**
- `breakfast`: Boolean - Whether breakfast was consumed
- `snackOne`: Boolean - Whether first snack was consumed  
- `meal`: Boolean - Whether main meal (lunch) was consumed
- `snackTwo`: Boolean - Whether second snack was consumed
- `dinner`: Boolean - Whether dinner was consumed

**Response (Success - 200):**
```json
{
  "message": "Daily record created successfully",
  "habitRecord": {
    "id": "record_789",
    "userId": "60f4b3b5b8f3a2001f5e4a1b",
    "date": "2023-07-19",
    "nutritionMeals": {
      "breakfast": true,
      "snackOne": false,
      "meal": true,
      "snackTwo": true,
      "dinner": true
    },
    "exerciseMinutes": 60,
    "sleepMinutes": 480,
    "hydrationMl": 2500,
    "notes": "Had a productive day"
  }
}
```

---

## 📊 Session-Based Endpoints

### 1. Get User Session by Date
**GET** `/api/habits/sessions/user/{userId}/date/{date}`

Get habit session with all periods for a specific date.

**Parameters:**
- `userId` (path): User ID
- `date` (path): Date in YYYY-MM-DD format

**Example:**
```
GET /api/habits/sessions/user/60f4b3b5b8f3a2001f5e4a1b/date/2023-07-19
```

**Response (Success - 200):**
```json
{
  "session": {
    "id": "session_123",
    "userId": "60f4b3b5b8f3a2001f5e4a1b",
    "date": "2023-07-19",
    "habitPeriods": [...],
    "scorePeriods": [...],
    "currentPeriodIndex": 2
  },
  "totalPeriods": 3,
  "totalScores": 2,
  "currentPeriodIndex": 2
}
```

---

### 2. Get All User Sessions
**GET** `/api/habits/sessions/user/{userId}`

Get all sessions for a user.

**Parameters:**
- `userId` (path): User ID

**Example:**
```
GET /api/habits/sessions/user/60f4b3b5b8f3a2001f5e4a1b
```

**Response (Success - 200):**
```json
{
  "sessions": [
    {
      "id": "session_123",
      "date": "2023-07-19",
      "totalPeriods": 3,
      "totalScores": 2
    }
  ],
  "totalSessions": 1
}
```

---

### 3. Create New Period
**POST** `/api/habits/sessions/new-period`

Manually create a new period (for testing time lapse).

**Request Body:**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "reason": "Testing time progression"
}
```

**Response (Success - 200):**
```json
{
  "message": "New period created successfully",
  "sessionId": "session_123",
  "totalPeriods": 4,
  "currentPeriodIndex": 3,
  "reason": "Testing time progression"
}
```

---

## 📈 Score Retrieval Endpoints

### 1. Get Latest Scores
**GET** `/api/habits/scores/user/{userId}/latest`

Get the latest calculated scores for a user.

**Parameters:**
- `userId` (path): User ID

**Example:**
```
GET /api/habits/scores/user/60f4b3b5b8f3a2001f5e4a1b/latest
```

**Response (Success - 200):**
```json
{
  "scoreId": "score_123",
  "periodId": "period_456",
  "calculationTime": "2023-07-19T10:30:00Z",
  "sessionId": "session_123",
  "date": "2023-07-19",
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "overallScore": 85,
  "scores": {
    "nutrition": 90,
    "exercise": 80,
    "sleep": 85,
    "hydration": 85
  },
  "hasData": true,
  "dataStatus": "complete",
  "notes": "All habits tracked successfully",
  "periodDetails": {
    "startTime": "2023-07-19T08:00:00Z",
    "endTime": "2023-07-19T10:30:00Z",
    "isActive": false,
    "nutrition": {
      "breakfast": true,
      "snackOne": true,
      "meal": true,
      "snackTwo": false,
      "dinner": false
    },
    "exerciseMinutes": 45,
    "sleepMinutes": 480,
    "hydrationMl": 2000
  }
}
```

---

### 2. Get Paginated Scores
**GET** `/api/habits/scores/user/{userId}`

Get paginated scores for a user with period details.

**Parameters:**
- `userId` (path): User ID
- `date` (query, optional): Date in YYYY-MM-DD format (defaults to today)
- `limit` (query, optional): Maximum scores to return (default: 10, max: 50)
- `offset` (query, optional): Number of scores to skip (default: 0)

**Example:**
```
GET /api/habits/scores/user/60f4b3b5b8f3a2001f5e4a1b?limit=5&offset=0
```

**Response (Success - 200):**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "date": "2023-07-19",
  "sessionId": "session_123",
  "scores": [
    {
      "scoreId": "score_123",
      "periodId": "period_456",
      "overallScore": 85,
      "scores": {
        "nutrition": 90,
        "exercise": 80,
        "sleep": 85,
        "hydration": 85
      },
      "calculationTime": "2023-07-19T10:30:00Z",
      "periodDetails": {...}
    }
  ],
  "pagination": {
    "total": 10,
    "limit": 5,
    "offset": 0,
    "hasMore": true,
    "returned": 5
  }
}
```

---

### 3. Get Score History
**GET** `/api/habits/scores/user/{userId}/history`

Get scores across multiple dates with pagination.

**Parameters:**
- `userId` (path): User ID
- `startDate` (query, optional): Start date (defaults to 7 days ago)
- `endDate` (query, optional): End date (defaults to today)
- `limit` (query, optional): Maximum sessions to return (default: 5, max: 20)
- `offset` (query, optional): Number of sessions to skip (default: 0)

**Example:**
```
GET /api/habits/scores/user/60f4b3b5b8f3a2001f5e4a1b/history?startDate=2023-07-15&endDate=2023-07-19&limit=3
```

**Response (Success - 200):**
```json
{
  "userId": "60f4b3b5b8f3a2001f5e4a1b",
  "dateRange": {
    "start": "2023-07-15",
    "end": "2023-07-19"
  },
  "sessions": [
    {
      "date": "2023-07-19",
      "sessionId": "session_123",
      "totalPeriods": 3,
      "totalScores": 2,
      "latestScore": 85,
      "lastActivity": "2023-07-19T10:30:00Z"
    },
    {
      "date": "2023-07-18",
      "sessionId": "session_122",
      "totalPeriods": 4,
      "totalScores": 3,
      "latestScore": 78,
      "lastActivity": "2023-07-18T22:15:00Z"
    }
  ],
  "pagination": {
    "limit": 3,
    "offset": 0,
    "returned": 2,
    "hasMore": false
  }
}
```

---

## 📝 Common Response Patterns

### Success Response Structure
```json
{
  "message": "Operation successful",
  "data": {...}
}
```

### Error Response Structure
```json
{
  "error": "Description of what went wrong"
}
```

---

## 🔄 RabbitMQ Integration

The system uses RabbitMQ for asynchronous habit score calculations:

1. **Daily Processing**: Triggered by scheduler, processes all records from previous day
2. **Individual Scoring**: Each habit record gets processed individually for scoring
3. **Queue Architecture**: 
   - Main queue: `habit.main.queue` (daily batch processing)
   - Individual queue: `habit.individual.queue` (per-record processing)

Score calculations happen automatically via the queue system and are available through the score endpoints once processed.

---

## 🛠️ Testing the API

### Using cURL

**Register a user:**
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

**Update exercise:**
```bash
curl -X POST http://localhost:8080/api/habits/update-exercise \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "USER_ID_HERE",
    "minutes": "45"
  }'
```

**Create daily record with meals:**
```bash
curl -X POST http://localhost:8080/api/habits/create-daily-record \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "USER_ID_HERE",
    "notes": "Great day!",
    "meals": {
      "breakfast": true,
      "snackOne": false,
      "meal": true,
      "snackTwo": true,
      "dinner": true
    },
    "exerciseMinutes": 60,
    "sleepMinutes": 480,
    "hydrationMl": 2500
  }'
```

**Get latest scores:**
```bash
curl -X GET http://localhost:8080/api/habits/scores/user/USER_ID_HERE/latest
```

---

## 📚 Additional Notes

- All timestamps are in ISO 8601 format (UTC)
- User IDs are MongoDB ObjectIds as strings
- Scores are calculated on a 0-100 scale
- The system automatically creates new time periods for continuous habit tracking
- RabbitMQ queues handle asynchronous score calculations with retry logic