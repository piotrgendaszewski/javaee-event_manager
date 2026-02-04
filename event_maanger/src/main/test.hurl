

### Note: Tests that modify the DB are destructive — back up `data/event_manager.sqlite` before running.

### Test 1: Login with valid credentials (admin)
POST http://localhost:8080/event_maanger_war_exploded/api/auth/login
Content-Type: application/json

{
  "login": "admin",
  "password": "admin"
}

HTTP 200
[Captures]
response_body: body

### Test 2: Login with invalid credentials
POST http://localhost:8080/event_maanger_war_exploded/api/auth/login
Content-Type: application/json

{
  "login": "invalid_user",
  "password": "wrong_password"
}

HTTP 401
[Captures]
response_body: body

### Test 3: Register new user (DESCTRUCTIVE - will modify DB)
# Backup DB first: copy data/event_manager.sqlite data/event_manager.sqlite.bak
POST http://localhost:8080/event_maanger_war_exploded/api/auth/register
Content-Type: application/json

{
  "login": "user",
  "password": "password",
  "email": "newuser@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "123456789",
  "address": "123 Main St"
}

HTTP 201
[Captures]
response_body: body

### Test 4: Register with missing email (should fail)
POST http://localhost:8080/event_maanger_war_exploded/api/auth/register
Content-Type: application/json

{
  "login": "anotheruser",
  "password": "pass123"
}

HTTP 400
[Captures]
response_body: body

### Test 5: Register with duplicate login (should fail)
POST http://localhost:8080/event_maanger_war_exploded/api/auth/register
Content-Type: application/json

{
  "login": "admin",
  "password": "password123",
  "email": "admin2@example.com",
  "firstName": "Admin2",
  "lastName": "User",
  "phoneNumber": "987654321",
  "address": "456 Oak St"
}

HTTP 409
[Captures]
response_body: body



### Test 6: Get all events (public) - Returns EventPublicDTO with pricing and availability
GET http://localhost:8080/event_maanger_war_exploded/api/public/events
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 7: Get event by ID (public) - Returns EventPublicDTO with pricing and availability
GET http://localhost:8080/event_maanger_war_exploded/api/public/events/1
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 9: Search events with filters (public) - Returns list of EventPublicDTO
GET http://localhost:8080/event_maanger_war_exploded/api/public/events/search?eventName=Jazz
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 11: Get remaining tickets for event (public)
GET http://localhost:8080/event_maanger_war_exploded/api/public/events/2/tickets/remaining
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 12: Get all users (admin) - with token
GET http://localhost:8080/event_maanger_war_exploded/api/admin/users
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 16: Get user by ID (admin)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/users/2
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 17: Create admin user with password (admin)
POST http://localhost:8080/event_maanger_war_exploded/api/admin/users/create-admin
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Content-Type: application/json

{
  "login": "newadmin3",
  "email": "newadmin@example.com",
  "passwordHash": "newadmin123",
  "firstName": "New",
  "lastName": "Admin"
}

HTTP 200
[Captures]
response_body: body

### Test 18: Update user (admin)
PUT http://localhost:8080/event_maanger_war_exploded/api/admin/users/5
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Content-Type: application/json

{
  "firstName": "Updated",
  "lastName": "User",
  "email": "updated@example.com"
}

HTTP 200
[Captures]
response_body: body

### Test 19: Delete user (admin)
DELETE http://localhost:8080/event_maanger_war_exploded/api/admin/users/13
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU

HTTP 204
[Captures]
response_body: body

### Test 30: Get all rooms (admin)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/events
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 21: Get event by ID (admin)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/events/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 22: Create event (admin)
POST http://localhost:8080/event_maanger_war_exploded/api/admin/events
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Content-Type: application/json

{
  "name": "New Conference 2035",
  "description": "Annual tech conference",
  "eventDate": "2026-06-15",
  "eventTime": "09:00",
  "eventStartDate": "2026-06-15",
  "eventEndDate": "2026-06-17",
  "numberedSeats": false,
  "ticketPrices": {"Standard": 99.99, "VIP": 199.99},
  "ticketQuantities": {"Standard": 300, "VIP": 50}
}

HTTP 200
[Captures]
response_body: body

### Test 23: Update event (admin)
PUT http://localhost:8080/event_maanger_war_exploded/api/admin/events/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Content-Type: application/json

{
  "name": "Updated Conference 2026",
  "description": "Annual tech conference - Updated",
  "eventDate": "2026-06-20",
  "eventTime": "09:00",
  "eventStartDate": "2026-06-20",
  "eventEndDate": "2026-06-22"
}

HTTP 200
[Captures]
response_body: body

### Test 24: Delete event (admin)
DELETE http://localhost:8080/event_maanger_war_exploded/api/admin/events/5
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU

HTTP 204
[Captures]
response_body: body



### Test 25: Get all locations (admin)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/locations
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 26: Get location by ID (admin)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/locations/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 27: Create location (admin)
POST http://localhost:8080/event_maanger_war_exploded/api/admin/locations
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Content-Type: application/json

{
  "name": "Convention Center 2",
  "address": "123 Convention Blvd",
  "maxAvailableSeats": 5000
}

HTTP 200
[Captures]
response_body: body

### Test 28: Update location (admin)
PUT http://localhost:8080/event_maanger_war_exploded/api/admin/locations/2
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Content-Type: application/json

{
  "name": "Hala Widowisków Updated",
  "address": "Kraków, ul. Armii Krajowej 15",
  "maxAvailableSeats": 3000
}

HTTP 200
[Captures]
response_body: body

### Test 29: Delete location (admin)
DELETE http://localhost:8080/event_maanger_war_exploded/api/admin/locations/4
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU

HTTP 204
[Captures]
response_body: body



### Test 30: Get all rooms (admin)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/rooms
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 31: Get room by ID (admin)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/rooms/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 32: Create room (admin)
POST http://localhost:8080/event_maanger_war_exploded/api/admin/rooms
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Content-Type: application/json

{
  "name": "Main Hall",
  "description": "Large conference hall",
  "seatCapacity": 500,
  "location": {"id": 1}
}

HTTP 200
[Captures]
response_body: body

### Test 33: Update room (admin)
PUT http://localhost:8080/event_maanger_war_exploded/api/admin/rooms/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Content-Type: application/json

{
  "name": "Updated Main Hall",
  "description": "Large conference hall - updated capacity",
  "seatCapacity": 600
}

HTTP 200
[Captures]
response_body: body

### Test 34: Delete room (admin)
DELETE http://localhost:8080/event_maanger_war_exploded/api/admin/rooms/4
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU

HTTP 204
[Captures]
response_body: body

### Test 35: Get user's tickets (authenticated user)
GET http://localhost:8080/event_maanger_war_exploded/api/private/tickets
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwidXNlcklkIjoyLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzcwMjMyMzkzLCJleHAiOjE3NzAzMTg3OTN9.kSHJy6WmFtPqPruv69gkCt-8pzCGtB6wQIBEOj-icGE
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 36: Get user's tickets with filters (authenticated user)
GET http://localhost:8080/event_maanger_war_exploded/api/private/tickets/search?validOnly=false
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwidXNlcklkIjoyLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzcwMjMyMzkzLCJleHAiOjE3NzAzMTg3OTN9.kSHJy6WmFtPqPruv69gkCt-8pzCGtB6wQIBEOj-icGE
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 37: Get specific ticket details (authenticated user)
GET http://localhost:8080/event_maanger_war_exploded/api/private/tickets/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwidXNlcklkIjoyLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzcwMjMyMzkzLCJleHAiOjE3NzAzMTg3OTN9.kSHJy6WmFtPqPruv69gkCt-8pzCGtB6wQIBEOj-icGE
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 37b: Get specific ticket details (authenticated user, wrong ticket ID) - should fail
GET http://localhost:8080/event_maanger_war_exploded/api/private/tickets/4
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwidXNlcklkIjoyLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzcwMjMyMzkzLCJleHAiOjE3NzAzMTg3OTN9.kSHJy6WmFtPqPruv69gkCt-8pzCGtB6wQIBEOj-icGE
Accept: application/json

HTTP 403
[Captures]
response_body: body

### Test 38: Get user's locations (authenticated user)
GET http://localhost:8080/event_maanger_war_exploded/api/private/locations
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwidXNlcklkIjoyLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzcwMjMyMzkzLCJleHAiOjE3NzAzMTg3OTN9.kSHJy6WmFtPqPruv69gkCt-8pzCGtB6wQIBEOj-icGE
Accept: application/json

HTTP 200
[Captures]
response_body: body

### Test 39: Get specific location details (authenticated user)
GET http://localhost:8080/event_maanger_war_exploded/api/private/locations/2
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwidXNlcklkIjoyLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzcwMjMyMzkzLCJleHAiOjE3NzAzMTg3OTN9.kSHJy6WmFtPqPruv69gkCt-8pzCGtB6wQIBEOj-icGE
Accept: application/json

HTTP 200
[Captures]
response_body: body



### Test 40: Access admin endpoint without token (should fail)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/events
Accept: application/json

HTTP 401
[Captures]
response_body: body

### Test 41: Access endpoint with invalid token (should fail)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/events
Authorization: Bearer invalid_token_here
Accept: application/json

HTTP 401
[Captures]
response_body: body

### Test 42: Invalid event ID (should fail)
GET http://localhost:8080/event_maanger_war_exploded/api/public/events/99999
Accept: application/json

HTTP 404
[Captures]
response_body: body

### Test 43: Create event with missing required field (should fail)
POST http://localhost:8080/event_maanger_war_exploded/api/admin/events
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Content-Type: application/json

{
  "description": "Missing name field",
  "eventDate": "2026-06-15"
}

HTTP 400
[Captures]
response_body: body

### Test 44: Invalid location ID (should fail)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/locations/99999
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Accept: application/json

HTTP 404
[Captures]
response_body: body

### Test 45: Invalid room ID (should fail)
GET http://localhost:8080/event_maanger_war_exploded/api/admin/rooms/99999
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzcwMjA1MjMyLCJleHAiOjE3NzAyOTE2MzJ9.8iLV_SSJI02ztoCfxyArkXXa7gFI2AW2XSC7HitfliU
Accept: application/json

HTTP 404
[Captures]
response_body: body

