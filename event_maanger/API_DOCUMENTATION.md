# Event Manager API - Authentication & Authorization

## Overview
System utilizuje JWT (JSON Web Tokens) do autoryzacji. Istnieją trzy rodzaje dostępu:
1. **Niezalogowani użytkownicy** - mogą przeglądać publiczne informacje o eventach
2. **Zalogowani użytkownicy** - mogą zarządzać swoimi biletami i opiniami
3. **Administratorzy** - pełny dostęp do wszystkich zasobów

## Authentication Endpoints

### 1. Rejestracja Użytkownika
```
POST /api/auth/register
Content-Type: application/json

{
  "login": "username",
  "password": "password123",
  "email": "user@example.com",
  "firstName": "Jan",
  "lastName": "Kowalski",
  "address": "ul. Testowa 1",
  "phoneNumber": "123456789"
}

Response: 201 Created
{
  "message": "User registered successfully",
  "login": "username"
}
```

### 2. Login
```
POST /api/auth/login
Content-Type: application/json

{
  "login": "username",
  "password": "password123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

### 3. Sprawdzenie Roli
```
GET /api/auth/role
Authorization: Bearer <token>

Response: 200 OK
{
  "username": "username",
  "userId": 1,
  "isAdmin": false,
  "role": "user"
}
```

## Public Endpoints (No Authentication Required)

### Przeglądanie Eventów
```
GET /api/public/events                    - Wszystkie eventy
GET /api/public/events/{id}              - Event po ID
GET /api/public/events/name/{name}       - Event po nazwie
GET /api/public/events/{eventId}/average-rating - Średnia ocena
```

## Authenticated User Endpoints

### Moje Bilety
```
GET /api/authenticated/user/tickets           - Moje bilety
GET /api/authenticated/user/tickets/{id}      - Szczegóły biletu
```

### Moje Opinie
```
GET /api/authenticated/user/reviews           - Moje opinie
POST /api/authenticated/user/reviews          - Dodaj opinię
PUT /api/authenticated/user/reviews/{id}      - Edytuj opinię
DELETE /api/authenticated/user/reviews/{id}   - Usuń opinię
```

**Wymagania dla opinii:**
- Rating: 1-5 gwiazdek
- Każdy użytkownik może mieć max 1 opinię na event
- Wymagane pola: event, rating, reviewDate

## Admin Endpoints (Requires Admin Role)

### Zarządzanie Eventami
```
GET /api/admin/events                    - Wszystkie eventy
POST /api/admin/events                   - Utwórz event
PUT /api/admin/events/{id}              - Edytuj event
DELETE /api/admin/events/{id}           - Usuń event
```

### Zarządzanie Lokalizacjami
```
GET /api/admin/locations                 - Wszystkie lokalizacje
POST /api/admin/locations                - Utwórz lokalizację
PUT /api/admin/locations/{id}           - Edytuj lokalizację
DELETE /api/admin/locations/{id}        - Usuń lokalizację
```

### Zarządzanie Pokojami
```
GET /api/admin/rooms                     - Wszystkie pokoje
POST /api/admin/rooms                    - Utwórz pokój
PUT /api/admin/rooms/{id}               - Edytuj pokój
DELETE /api/admin/rooms/{id}            - Usuń pokój
```

### Zarządzanie Biletami
```
GET /api/admin/tickets                   - Wszystkie bilety
DELETE /api/admin/tickets/{id}           - Usuń bilet
```

### Zarządzanie Opiniami
```
GET /api/admin/reviews                   - Wszystkie opinie
DELETE /api/admin/reviews/{id}           - Usuń opinię
```

### Zarządzanie Użytkownikami
```
GET /api/admin/users                     - Wszyscy użytkownicy
```

## Uwierzytelnianie Requestów

Wszystkie requesty wymagające autentykacji muszą zawierać header:
```
Authorization: Bearer <JWT_TOKEN>
```

## Kody Błędów

- `200 OK` - Pomyślnie
- `201 Created` - Zasób utworzony
- `400 Bad Request` - Błędne dane wejściowe
- `401 Unauthorized` - Brak/nieważny token
- `403 Forbidden` - Brak uprawnień/nie administrator
- `404 Not Found` - Zasób nie znaleziony
- `409 Conflict` - Konflikt (np. użytkownik już istnieje)
- `500 Internal Server Error` - Błąd serwera

## Klasy Bezpieczeństwa

### JwtUtil
- `generateToken()` - Generuje JWT token
- `validateToken()` - Waliduje token
- `getUserIdFromToken()` - Pobiera ID użytkownika z tokena
- `isAdminFromToken()` - Sprawdza czy użytkownik to admin

### JwtFilter
- Automatycznie waliduje tokeny
- Pozwala dostęp do /auth i /public bez tokena
- Dodaje informacje z tokena do kontekstu requestu

### AuthService
- `registerUser()` - Rejestruje nowego użytkownika
- `login()` - Loguje użytkownika i zwraca token
- Haszuje hasła za pomocą SHA-256

