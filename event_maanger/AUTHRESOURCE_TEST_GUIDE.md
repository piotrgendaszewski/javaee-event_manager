# AuthResource Tests - Dokumentacja

## Testy JUnit dla AuthResource

W pliku `src/test/java/REST/AuthResourceTest.java` znajduje się kompleksowy zestaw testów dla endpointów autentykacji.

### Struktura testów

Testy używają **Jersey Test Framework** do testowania REST endpointów:
- `POST /auth/register` - rejestracja nowego użytkownika
- `POST /auth/login` - logowanie użytkownika

### Testy warte uwagi

#### Testy walidacji polów

```java
@Test
@DisplayName("Should return 400 for missing login in registration")
void testRegisterWithMissingLogin()
```

Weryfikuje, że endpoint zwraca `400 Bad Request` gdy brakuje wymaganego pola.

#### Testy mapowania endpointów

```java
@Test
@DisplayName("Should map POST /auth/register endpoint")
void testRegisterEndpointExists()
```

Potwierdza, że endpointy są prawidłowo mapowane w Jersey.

#### Testy bezpieczeństwa

```java
@Test
@DisplayName("Should not expose internal exceptions in error messages")
void testErrorMessageSafety()
```

Weryfikuje, że błędy wewnętrzne nie są eksponowane w odpowiedziach.

#### Testy typu contentu

```java
@Test
@DisplayName("Should respond with JSON content type")
void testResponseContentType()
```

Potwierdza, że odpowiedzi są w formacie JSON.

## Uruchamianie testów

### Z Maven

```bash
mvn test -Dtest=AuthResourceTest
```

### Z IDE (IntelliJ IDEA)

1. Prawy klik na `AuthResourceTest.java` → **Run 'AuthResourceTest'**
2. Lub: Ctrl+Shift+F10 (Windows)

### Uruchomienie pojedynczego testu

```bash
mvn test -Dtest=AuthResourceTest#testRegisterWithMissingLogin
```

## Test HTML - Alternatywa

Dla testowania bez JUnit, użyj interaktywnego testu HTML:

```
http://localhost:8080/event_maanger/auth-test.html
```

Zawiera:
- ✅ Formularz rejestracji z walidacją
- ✅ Formularz logowania z tokenami JWT
- ✅ Szybkie testy predefiniowane
- ✅ Wizualizacja odpowiedzi API

## Skrypty CURL

Jeśli preferujesz testowanie z linii poleceń:

### Rejestracja

```bash
curl -X POST http://localhost:8080/event_maanger/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "login":"testuser",
    "email":"test@example.com",
    "password":"password123",
    "firstName":"Test",
    "lastName":"User"
  }'
```

### Logowanie

```bash
curl -X POST http://localhost:8080/event_maanger/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login":"testuser",
    "password":"password123"
  }'
```

## Testy Bash/PowerShell

Dla automatyzacji, użyj skryptów testowych:

### Linux/Mac

```bash
./auth-test.sh
```

### Windows

```powershell
.\auth-test.bat
```

## Czego testują

| Test | Cel |
|------|-----|
| `testRegisterWithMissingLogin` | Walidacja wymaganego pola login |
| `testRegisterWithMissingEmail` | Walidacja wymaganego pola email |
| `testRegisterWithMissingPassword` | Walidacja wymaganego pola password |
| `testLoginWithMissingLogin` | Walidacja pola login w logowaniu |
| `testLoginWithMissingPassword` | Walidacja pola password w logowaniu |
| `testLoginWithEmptyRequest` | Obsługa pustego body żądania |
| `testRegisterEndpointExists` | Mapa endpoint `/auth/register` |
| `testLoginEndpointExists` | Mapa endpoint `/auth/login` |
| `testResponseContentType` | Typ contentu JSON |
| `testErrorMessageSafety` | Nie ekspozycja błędów SQL |

## Oczekiwane Kody HTTP

| Scenariusz | Kod | Przykład |
|-----------|-----|---------|
| Sukces rejestracji | 201 | Created |
| Sukces logowania | 200 | OK |
| Brakujące pole | 400 | Bad Request |
| Nieprawidłowe hasło | 401 | Unauthorized |
| Login już istnieje | 409 | Conflict |
| Błąd serwera | 500 | Internal Server Error |

## Notatki techniczne

- Testy używają **Jersey Test Framework** z wbudowanym konektorem (`InMemoryConnector`)
- Każdy test jest niezależny i mogą być uruchamiane w dowolnej kolejności
- Testy weryfikują HTTP responses, nie logikę biznesową DAO/Service
- Do pełnego testowania integracyjnego użyj test HTML lub skryptów CURL

## Rozwiązywanie problemów

### Problem: SQLITE_BUSY

```
[SQLITE_BUSY] The database file is locked (database is locked)
```

**Rozwiązanie:**
1. Zamknij wszystkie procesy używające `event_manager.sqlite`
2. Usuń pliki `sqlite-*.jar` z temp
3. Przebuduj: `mvn clean test`

### Problem: Test timeout

Jeśli testy trwają zbyt długo, zwiększ timeout:

```bash
mvn test -Dtest=AuthResourceTest -DtestFailureIgnore=false -Dmaven.test.timeout=30000
```

### Problem: Brak uprawnień

Upewnij się, że katalog `data/` ma uprawnienia do zapisu:

```bash
chmod 755 data/
```

## Następne kroki

Aby ulepszyć testy:

1. ✅ Mockować `AuthService` zamiast całego `UserHibernate` (wymaga refaktoringu)
2. ✅ Dodać testy integracyjne z rzeczywistą bazą danych
3. ✅ Dodać testy wydajności/load testing
4. ✅ Dodać testy bezpieczeństwa (SQL injection, XSS itp.)

---

**Ostatnia aktualizacja:** Luty 2026

