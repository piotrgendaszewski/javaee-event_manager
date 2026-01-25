# JWT Authentication Testing Guide

## Quick Start

### 1. Build and Run
```bash
mvn clean package
# Deploy WAR to Tomcat
```

## Testing JWT Authentication

### Default Admin Account
- **Login:** `admin`
- **Password:** `admin`
- **Email:** `admin@admin.com`
- Automatically created on first run if database is empty

---

## API Testing Examples

### 1. User Registration
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "login": "user1",
    "password": "password123",
    "email": "user1@example.com",
    "firstName": "Jan",
    "lastName": "Kowalski",
    "address": "ul. Testowa 1",
    "phoneNumber": "123456789"
  }'
```

### 2. User Login (Get JWT Token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "admin",
    "password": "admin"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "message": "Login successful"
}
```

### 3. Check User Role (Requires Token)
```bash
curl -X GET http://localhost:8080/api/auth/role \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

**Response:**
```json
{
  "username": "admin",
  "userId": 1,
  "isAdmin": true,
  "role": "admin"
}
```

### 4. Access Public Events (No Token Required)
```bash
curl -X GET http://localhost:8080/api/public/events
```

### 5. Access Protected Authenticated User Endpoint (Requires Token)
```bash
curl -X GET http://localhost:8080/api/authenticated/user/tickets \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### 6. Access Admin Endpoints (Requires Admin Token)
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN_HERE"
```

---

## How JWT Authentication Works

1. **Registration** → User creates account via `/api/auth/register`
2. **Login** → User logs in via `/api/auth/login` → Receives JWT token
3. **Token Usage** → User sends token in `Authorization: Bearer <token>` header
4. **Filter** → JwtFilter validates token on every request
5. **Access Control** → Resources check user role (admin/user) from token

---

## Security Notes

- Passwords are hashed with SHA-256
- JWT tokens expire after 24 hours
- Token secret key is fixed for consistency across restarts
- Admin users created automatically on first run (database empty)
- Each user can have only 1 review per event
- Numbered seat events enforce unique seat booking

---

## Token Structure

JWT tokens contain:
- **Subject:** username
- **Claim - userId:** numeric user ID
- **Claim - isAdmin:** boolean admin flag
- **Issued At:** token creation timestamp
- **Expiration:** 24 hours from creation

---

## Troubleshooting

### "Missing or invalid Authorization header"
- Make sure to include `Authorization: Bearer <token>` header
- Token format must be: `Bearer eyJhbGc...`

### "Invalid or expired token"
- Token has expired (24 hour limit)
- Token secret key doesn't match (server restart?)
- Token is malformed

### "Admin access required"
- Only admin users can access `/api/admin/*` endpoints
- Login with admin account or ask admin to promote user

### "You have already reviewed this event"
- Each user can only add 1 review per event
- Use PUT endpoint to update existing review

---

## Default Endpoint Access Levels

| Endpoint | Auth Required | Admin Required | Description |
|----------|---------------|----------------|-------------|
| `/api/auth/register` | No | No | User registration |
| `/api/auth/login` | No | No | User login |
| `/api/auth/role` | Yes | No | Check user role |
| `/api/public/events` | No | No | View all events (public) |
| `/api/authenticated/user/*` | Yes | No | User's tickets/reviews |
| `/api/admin/*` | Yes | Yes | Admin management |


