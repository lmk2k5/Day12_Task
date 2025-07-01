# Day 12 - Event Ticket Token System

## Task Description

Design and implement a secure, scalable Java-based Event Ticket Token System using MongoDB, JWT authentication, and asynchronous email delivery.

## 1. User Registration & Account Management

Users register using their email and name. Upon registration:

- Generate a strong, random password for the user.
- Hash the password (bcrypt or Argon2) before storing it in MongoDB.
- Email the password to the user using an async SMTP service.
- Prevent duplicate registration by email.
- Support for password reset via email:
  - Generate a secure, time-limited reset token.
  - Send the token via email.

## 2. Secure Authentication

Use JWT (JSON Web Tokens) for authentication.

- Users log in with email and password.
- On success, receive a JWT for subsequent API requests.
- Implement token expiry and a token refresh endpoint.

## 3. Event Management & Token Booking

Users can view a paginated list of upcoming events, each with a limited and configurable number of tokens.

Event details include:
- Name
- Description
- Time
- Location
- Token limit
- Status (upcoming, active, closed)

Admin-only functionality:
- Create, edit, and delete events (role-based access)

Token booking:
- Users can book a token for a specific event if tokens are available
- Generate a unique booking token (secure alphanumeric string or QR code image)
- Store bookings with user, event, timestamp, and token
- Reduce the event’s available token count atomically (using MongoDB transactions or optimistic locking)
- Send the booking token to the user's email asynchronously (SMTP)
- Users can view their bookings and retrieve their tokens

## 4. RESTful API Design

Use appropriate HTTP status codes.

APIs should include:
- Registration, login, password reset (initiate and complete)
- List events (with filtering, sorting, and pagination)
- Admin event management (CRUD)
- Book event token, view user bookings
- Token validation endpoint (for event staff to verify at entry)

## 5. Security and Best Practices

- Never store or log plain-text passwords
- Input validation and sanitization on all endpoints
- Use role-based access control (user, admin)
- Audit important actions such as registration, booking, and event modifications
- Use environment-based configuration for SMTP, JWT secrets, database URI, etc.
