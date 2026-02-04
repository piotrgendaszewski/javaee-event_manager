# Dokumentacja DTO dla Public API - Event Manager

## Podsumowanie zmian

Wszystkie endpointy publiczne (`/api/public/*`) zostały zaktualizowane aby zwracały **DTO (Data Transfer Objects)** zamiast pełnych obiektów modelu. To redukuje ilość danych w odpowiedziach i ukrywa wrażliwe informacje.

---

## EventPublicDTO

**Zastosowanie:** Wszystkie endpointy związane z wydarzeniami w sekcji `/public/events`

**Zawarte pola:**
- `id` - identyfikator wydarzenia
- `name` - nazwa wydarzenia
- `description` - opis
- `eventStartDate` - data rozpoczęcia (YYYY-MM-DD)
- `eventEndDate` - data zakończenia (YYYY-MM-DD)
- `averageRating` - średnia ocena z recenzji (0.0-5.0)
- `ticketAvailability` - mapa dostępności biletów

### Struktura ticketAvailability

```json
{
  "ticketType": {
    "price": 99.99,
    "availableCount": 150
  }
}
```

### Przykładowa odpowiedź

```json
{
  "id": 1,
  "name": "Festiwal Jazzowy 2026",
  "description": "Międzynarodowy festiwal muzyki jazzowej",
  "eventStartDate": "2026-06-15",
  "eventEndDate": "2026-06-17",
  "averageRating": 4.5,
  "ticketAvailability": {
    "Standard": {
      "price": 50.0,
      "availableCount": 100
    },
    "VIP": {
      "price": 100.0,
      "availableCount": 25
    },
    "Premium": {
      "price": 150.0,
      "availableCount": 10
    }
  }
}
```

### Endpointy zwracające EventPublicDTO

| Endpoint | Metoda | Zwracane |
|----------|--------|----------|
| `/api/public/events` | GET | `List<EventPublicDTO>` |
| `/api/public/events/{id}` | GET | `EventPublicDTO` |
| `/api/public/events/name/{name}` | GET | `EventPublicDTO` |
| `/api/public/events/search` | GET | `List<EventPublicDTO>` |

---

## EventReviewPublicDTO

**Zastosowanie:** Wszystkie endpointy związane z recenzjami w sekcji `/public/events/{eventId}/reviews`

**Zawarte pola:**
- `id` - identyfikator recenzji
- `rating` - ocena (1-5)
- `reviewText` - tekst recenzji
- `reviewDate` - data recenzji (YYYY-MM-DD)
- `reviewerName` - imię i nazwisko recenzenta (bez wrażliwych danych)

### Przykładowa odpowiedź

```json
[
  {
    "id": 1,
    "rating": 5,
    "reviewText": "Fantastyczne wydarzenie! Polecam wszystkim.",
    "reviewDate": "2026-06-18",
    "reviewerName": "Jan Kowalski"
  },
  {
    "id": 2,
    "rating": 4,
    "reviewText": "Dobra organizacja, mogłoby być więcej atrakcji.",
    "reviewDate": "2026-06-19",
    "reviewerName": "Maria Nowak"
  }
]
```

### Endpointy zwracające EventReviewPublicDTO

| Endpoint | Metoda | Zwracane |
|----------|--------|----------|
| `/api/public/events/{eventId}/reviews` | GET | `List<EventReviewPublicDTO>` |
| `/api/public/events/{eventId}/reviews/search` | GET | `List<EventReviewPublicDTO>` |

---

## Filtry dostępne dla recenzji

**Query parameters dla `/api/public/events/{eventId}/reviews/search`:**

- `startDate` (string, YYYY-MM-DD) - filtruj recenzje od tej daty
- `endDate` (string, YYYY-MM-DD) - filtruj recenzje do tej daty
- `minRating` (integer, 1-5) - minimalna ocena
- `maxRating` (integer, 1-5) - maksymalna ocena
- `sortBy` (string, default: "date") - sortuj po: "date" lub "rating"
- `sortOrder` (string, default: "desc") - kolejność: "asc" lub "desc"

### Przykład

```
GET /api/public/events/1/reviews/search?minRating=4&maxRating=5&sortBy=rating&sortOrder=desc
```

---

## Filtry dostępne dla zdarzeń

**Query parameters dla `/api/public/events/search`:**

- `eventName` (string) - fragment nazwy (case-insensitive)
- `locationName` (string) - fragment nazwy lokalizacji
- `startDate` (string, YYYY-MM-DD) - data rozpoczęcia (>=)
- `endDate` (string, YYYY-MM-DD) - data zakończenia (<=)
- `minPrice` (double) - minimalna cena biletu
- `maxPrice` (double) - maksymalna cena biletu
- `onlyAvailable` (boolean) - tylko wydarzenia z dostępnymi biletami

### Przykład

```
GET /api/public/events/search?eventName=Jazz&minPrice=50&maxPrice=150&onlyAvailable=true
```

---

## Korzyści DTO

✅ **Redukcja danych** - mniejsze payload w odpowiedziach  
✅ **Bezpieczeństwo** - ukrywanie wrażliwych danych (np. email, hasła)  
✅ **Prostota** - zwracanie tylko istotnych informacji  
✅ **Wydajność** - mniej danych = szybsza transmisja  
✅ **Elastyczność** - łatwiejsze zmiany API bez wpływu na model  

---

## Przesłanie odpowiedzi HTTP

Wszystkie endpointy publiczne mają:
- **Content-Type**: `application/json`
- **Status**: `200 OK` (przy sukcesie) lub `404 Not Found` (gdy brak zasobu)

