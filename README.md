# Globetrotter Backend (Java Version)

This is a Java Spring Boot conversion of the original Node.js Globetrotter backend application.

## Technology Stack

- Java 17
- Spring Boot 3.2.4
- Spring Data JPA
- Spring Security with JWT
- MySQL Database

## Project Structure

- **config**: Contains configuration files (Security, etc.)
- **controller**: REST API controllers
- **model**: Entity classes
- **repository**: JPA repositories for database access
- **security**: JWT authentication components
- **service**: Business logic interfaces and implementations

## Getting Started

### Prerequisites

- Java 17+
- Maven
- MySQL Database

### Environment Variables

The application uses the following environment variables:

```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=globetrotter
DB_USER=your_db_username
DB_PASS=your_db_password
JWT_SECRET=your_jwt_secret
PORT=8080
```

### Running the Application

1. Clone the repository
2. Set up environment variables
3. Run the following commands:

```bash
cd globetrotter_java
mvn clean install
mvn spring-boot:run
```

## API Endpoints

### Authentication

- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login existing user

### Cities

- `GET /cities` - Get all cities
- `GET /cities/{id}` - Get city by ID

### Quiz

- `POST /quiz/start` - Start a new quiz session
- `POST /quiz/answer` - Submit an answer
- `GET /quiz/result/{sessionId}` - Get quiz results

### Challenge

- `POST /challenge/create` - Create a new challenge
- `POST /challenge/accept/{inviteLink}` - Accept a challenge
- `GET /challenge/result/{challengeId}` - Get challenge results

## Database Schema

The application uses the same database schema as the original Node.js version, with tables for:
- users
- cities
- quiz_sessions
- challenges
- and more

## Differences from Node.js Version

This Java version follows the same functional requirements as the original Node.js implementation but uses:
- Object-Oriented approach with Java classes
- Spring Boot framework for REST API development
- JPA for database interactions
- Spring Security for authentication

## License

This project is licensed under the ISC License. # globetrotter-be
