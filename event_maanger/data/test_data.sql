-- Test Data for Event Manager SQLite Database
-- Wypełnienie bazy danymi testowymi z zachowaniem relacji między tabelami

-- ========== USERS ==========
-- Admin user: login=admin, password=admin
INSERT OR IGNORE INTO users (id, login, email, first_name, last_name, address, phone_number, password_hash, is_admin)
VALUES (1, 'admin', 'admin@admin.com', 'Admin', 'Administrator', 'System', '000-000-0000',
        '$2a$10$1xHjxrFN7QdJZ7GnBp5ym.v9J1j.Zqq3L3K8Z5Q6R7S8T9U0V1W2', 1);

-- Regular users
INSERT OR IGNORE INTO users (id, login, email, first_name, last_name, address, phone_number, password_hash, is_admin)
VALUES (2, 'john_doe', 'john@example.com', 'John', 'Doe', 'ul. Główna 10, Warszawa', '+48-600-100-100',
        '$2a$10$test_hash_john_doe', 0);

INSERT OR IGNORE INTO users (id, login, email, first_name, last_name, address, phone_number, password_hash, is_admin)
VALUES (3, 'jane_smith', 'jane@example.com', 'Jane', 'Smith', 'ul. Długa 20, Kraków', '+48-600-200-200',
        '$2a$10$test_hash_jane_smith', 0);

INSERT OR IGNORE INTO users (id, login, email, first_name, last_name, address, phone_number, password_hash, is_admin)
VALUES (4, 'bob_wilson', 'bob@example.com', 'Bob', 'Wilson', 'ul. Krótka 5, Gdańsk', '+48-600-300-300',
        '$2a$10$test_hash_bob_wilson', 0);

INSERT OR IGNORE INTO users (id, login, email, first_name, last_name, address, phone_number, password_hash, is_admin)
VALUES (5, 'alice_brown', 'alice@example.com', 'Alice', 'Brown', 'ul. Spacerowa 15, Wrocław', '+48-600-400-400',
        '$2a$10$test_hash_alice_brown', 0);

-- ========== LOCATIONS ==========
INSERT OR IGNORE INTO locations (id, name, address, max_available_seats)
VALUES (1, 'Pałac Kultury', 'Warszawa, pl. Defilad 1', 3000);

INSERT OR IGNORE INTO locations (id, name, address, max_available_seats)
VALUES (2, 'Hala Widowisków', 'Kraków, ul. Armii Krajowej 10', 2500);

INSERT OR IGNORE INTO locations (id, name, address, max_available_seats)
VALUES (3, 'Stadion Energa', 'Gdańsk, ul. Pokoleń 1', 4000);

INSERT OR IGNORE INTO locations (id, name, address, max_available_seats)
VALUES (4, 'Ama Silesia', 'Chorzów, ul. Hodowlana 3', 2000);

-- ========== ROOMS ==========
INSERT OR IGNORE INTO rooms (id, name, description, location_id, seat_capacity)
VALUES (1, 'Sala Główna', 'Sala główna z nowoczesnym wyposażeniem', 1, 1500);

INSERT OR IGNORE INTO rooms (id, name, description, location_id, seat_capacity)
VALUES (2, 'Sala Kameralna', 'Intymna sala dla kameralnych imprez', 1, 400);

INSERT OR IGNORE INTO rooms (id, name, description, location_id, seat_capacity)
VALUES (3, 'Studio', 'Nowoczesne studio nagrań', 1, 150);

INSERT OR IGNORE INTO rooms (id, name, description, location_id, seat_capacity)
VALUES (4, 'Sala Koncertowa', 'Profesjonalna sala koncertowa', 2, 1200);

INSERT OR IGNORE INTO rooms (id, name, description, location_id, seat_capacity)
VALUES (5, 'Sala Ekspozycji', 'Przestrzeń dla wystawiennictwa', 2, 800);

INSERT OR IGNORE INTO rooms (id, name, description, location_id, seat_capacity)
VALUES (6, 'Arena Główna', 'Duża arena sportowa', 3, 2000);

INSERT OR IGNORE INTO rooms (id, name, description, location_id, seat_capacity)
VALUES (7, 'Hala Treningowa', 'Hala do treningów', 3, 1500);

-- ========== EVENTS ==========
INSERT OR IGNORE INTO events (id, name, description, event_date, event_time, event_start_date, event_end_date, numbered_seats)
VALUES (1, 'Koncert Rockowy 2026', 'Wielki koncert zespołu The Rock Stars', '2026-05-15', '19:00', '2026-05-15', '2026-05-15', 1);

INSERT OR IGNORE INTO events (id, name, description, event_date, event_time, event_start_date, event_end_date, numbered_seats)
VALUES (2, 'Jazz Festival', 'Międzynarodowy festiwal jazzu', '2026-06-01', '18:00', '2026-06-01', '2026-06-03', 0);

INSERT OR IGNORE INTO events (id, name, description, event_date, event_time, event_start_date, event_end_date, numbered_seats)
VALUES (3, 'Turniej Tenisa', 'Krajowy turniej tenisa ziemnego', '2026-07-10', '09:00', '2026-07-10', '2026-07-17', 1);

INSERT OR IGNORE INTO events (id, name, description, event_date, event_time, event_start_date, event_end_date, numbered_seats)
VALUES (4, 'Выставка Искусства', 'Międzynarodowa wystawa sztuki współczesnej', '2026-04-20', '10:00', '2026-04-20', '2026-05-30', 0);

INSERT OR IGNORE INTO events (id, name, description, event_date, event_time, event_start_date, event_end_date, numbered_seats)
VALUES (5, 'Maraton Warszawa 2026', 'Międzynarodowy maraton', '2026-08-15', '07:00', '2026-08-15', '2026-08-15', 1);

-- ========== EVENT_LOCATIONS ==========
INSERT OR IGNORE INTO event_locations (event_id, location_id)
VALUES (1, 1); -- Koncert Rockowy -> Pałac Kultury

INSERT OR IGNORE INTO event_locations (event_id, location_id)
VALUES (2, 2); -- Jazz Festival -> Hala Widowisków

INSERT OR IGNORE INTO event_locations (event_id, location_id)
VALUES (3, 3); -- Turniej Tenisa -> Stadion Energa

INSERT OR IGNORE INTO event_locations (event_id, location_id)
VALUES (4, 1); -- Wystawka Sztuki -> Pałac Kultury

INSERT OR IGNORE INTO event_locations (event_id, location_id)
VALUES (5, 3); -- Maraton -> Stadion Energa

-- ========== EVENT_ROOMS ==========
INSERT OR IGNORE INTO event_rooms (event_id, room_id)
VALUES (1, 1); -- Koncert Rockowy -> Sala Główna
INSERT OR IGNORE INTO event_rooms (event_id, room_id)
VALUES (1, 2); -- Koncert Rockowy -> Sala Kameralna (second stage)

INSERT OR IGNORE INTO event_rooms (event_id, room_id)
VALUES (2, 4); -- Jazz Festival -> Sala Koncertowa
INSERT OR IGNORE INTO event_rooms (event_id, room_id)
VALUES (2, 5); -- Jazz Festival -> Sala Ekspozycji

INSERT OR IGNORE INTO event_rooms (event_id, room_id)
VALUES (3, 6); -- Turniej Tenisa -> Arena Główna

INSERT OR IGNORE INTO event_rooms (event_id, room_id)
VALUES (4, 1); -- Wystawka Sztuki -> Sala Główna

INSERT OR IGNORE INTO event_rooms (event_id, room_id)
VALUES (5, 6); -- Maraton -> Arena Główna
INSERT OR IGNORE INTO event_rooms (event_id, room_id)
VALUES (5, 7); -- Maraton -> Hala Treningowa

-- ========== TICKET_PRICES ==========
INSERT OR IGNORE INTO ticket_prices (event_id, ticket_type, price)
VALUES (1, 'VIP', 150.00);
INSERT OR IGNORE INTO ticket_prices (event_id, ticket_type, price)
VALUES (1, 'STANDARD', 75.00);
INSERT OR IGNORE INTO ticket_prices (event_id, ticket_type, price)
VALUES (1, 'ECONOMY', 40.00);

INSERT OR IGNORE INTO ticket_prices (event_id, ticket_type, price)
VALUES (2, 'PREMIUM', 120.00);
INSERT OR IGNORE INTO ticket_prices (event_id, ticket_type, price)
VALUES (2, 'STANDARD', 60.00);

INSERT OR IGNORE INTO ticket_prices (event_id, ticket_type, price)
VALUES (3, 'STANDARD', 50.00);
INSERT OR IGNORE INTO ticket_prices (event_id, ticket_type, price)
VALUES (3, 'EARLY_BIRD', 35.00);

INSERT OR IGNORE INTO ticket_prices (event_id, ticket_type, price)
VALUES (4, 'FREE', 0.00);

INSERT OR IGNORE INTO ticket_prices (event_id, ticket_type, price)
VALUES (5, 'PARTICIPANT', 80.00);
INSERT OR IGNORE INTO ticket_prices (event_id, ticket_type, price)
VALUES (5, 'SPECTATOR', 30.00);

-- ========== TICKET_QUANTITIES ==========
INSERT OR IGNORE INTO ticket_quantities (event_id, ticket_type, quantity)
VALUES (1, 'VIP', 200);
INSERT OR IGNORE INTO ticket_quantities (event_id, ticket_type, quantity)
VALUES (1, 'STANDARD', 800);
INSERT OR IGNORE INTO ticket_quantities (event_id, ticket_type, quantity)
VALUES (1, 'ECONOMY', 500);

INSERT OR IGNORE INTO ticket_quantities (event_id, ticket_type, quantity)
VALUES (2, 'PREMIUM', 300);
INSERT OR IGNORE INTO ticket_quantities (event_id, ticket_type, quantity)
VALUES (2, 'STANDARD', 600);

INSERT OR IGNORE INTO ticket_quantities (event_id, ticket_type, quantity)
VALUES (3, 'STANDARD', 1000);
INSERT OR IGNORE INTO ticket_quantities (event_id, ticket_type, quantity)
VALUES (3, 'EARLY_BIRD', 500);

INSERT OR IGNORE INTO ticket_quantities (event_id, ticket_type, quantity)
VALUES (4, 'FREE', 2000);

INSERT OR IGNORE INTO ticket_quantities (event_id, ticket_type, quantity)
VALUES (5, 'PARTICIPANT', 1000);
INSERT OR IGNORE INTO ticket_quantities (event_id, ticket_type, quantity)
VALUES (5, 'SPECTATOR', 2000);

-- ========== TICKETS ==========
-- Tickets for user john_doe (id=2)
INSERT OR IGNORE INTO tickets (id, event_id, user_id, ticket_type, price, purchase_date, valid_from_date, valid_to_date, seat_number)
VALUES (1, 1, 2, 'VIP', 150.00, '2026-05-01', '2026-05-01', '2026-05-15', '101');

INSERT OR IGNORE INTO tickets (id, event_id, user_id, ticket_type, price, purchase_date, valid_from_date, valid_to_date, seat_number)
VALUES (2, 1, 2, 'STANDARD', 75.00, '2026-05-02', '2026-05-02', '2026-05-15', '205');

INSERT OR IGNORE INTO tickets (id, event_id, user_id, ticket_type, price, purchase_date, valid_from_date, valid_to_date, seat_number)
VALUES (3, 2, 2, 'PREMIUM', 120.00, '2026-05-15', '2026-06-01', '2026-06-03', NULL);

-- Tickets for user jane_smith (id=3)
INSERT OR IGNORE INTO tickets (id, event_id, user_id, ticket_type, price, purchase_date, valid_from_date, valid_to_date, seat_number)
VALUES (4, 1, 3, 'STANDARD', 75.00, '2026-05-05', '2026-05-05', '2026-05-15', '310');

INSERT OR IGNORE INTO tickets (id, event_id, user_id, ticket_type, price, purchase_date, valid_from_date, valid_to_date, seat_number)
VALUES (5, 3, 3, 'EARLY_BIRD', 35.00, '2026-06-01', '2026-07-10', '2026-07-17', '405');

-- Tickets for user bob_wilson (id=4)
INSERT OR IGNORE INTO tickets (id, event_id, user_id, ticket_type, price, purchase_date, valid_from_date, valid_to_date, seat_number)
VALUES (6, 2, 4, 'STANDARD', 60.00, '2026-05-20', '2026-06-01', '2026-06-03', NULL);

INSERT OR IGNORE INTO tickets (id, event_id, user_id, ticket_type, price, purchase_date, valid_from_date, valid_to_date, seat_number)
VALUES (7, 4, 4, 'FREE', 0.00, '2026-04-15', '2026-04-20', '2026-05-30', NULL);

-- Tickets for user alice_brown (id=5)
INSERT OR IGNORE INTO tickets (id, event_id, user_id, ticket_type, price, purchase_date, valid_from_date, valid_to_date, seat_number)
VALUES (8, 5, 5, 'PARTICIPANT', 80.00, '2026-07-01', '2026-08-15', '2026-08-15', '5001');

INSERT OR IGNORE INTO tickets (id, event_id, user_id, ticket_type, price, purchase_date, valid_from_date, valid_to_date, seat_number)
VALUES (9, 1, 5, 'ECONOMY', 40.00, '2026-05-08', '2026-05-08', '2026-05-15', '501');

-- ========== EVENT_REVIEWS ==========
INSERT OR IGNORE INTO event_reviews (id, event_id, user_id, rating, review_text, review_date)
VALUES (1, 1, 2, 5, 'Fantastyczny koncert! Wspaniała atmosfera i świetna muzyka.', '2026-05-16');

INSERT OR IGNORE INTO event_reviews (id, event_id, user_id, rating, review_text, review_date)
VALUES (2, 1, 3, 4, 'Bardzo dobry koncert, choć trochę długi program.', '2026-05-17');

INSERT OR IGNORE INTO event_reviews (id, event_id, user_id, rating, review_text, review_date)
VALUES (3, 1, 5, 5, 'Najlepszy koncert roku! Polecam wszystkim.', '2026-05-16');

INSERT OR IGNORE INTO event_reviews (id, event_id, user_id, rating, review_text, review_date)
VALUES (4, 2, 2, 3, 'Ciekawe, ale brakło tradycyjnego jazzu.', '2026-06-04');

INSERT OR IGNORE INTO event_reviews (id, event_id, user_id, rating, review_text, review_date)
VALUES (5, 2, 4, 4, 'Dobrze zorganizowany festiwal, warta polecenia.', '2026-06-04');

INSERT OR IGNORE INTO event_reviews (id, event_id, user_id, rating, review_text, review_date)
VALUES (6, 3, 3, 5, 'Turniej tenisa najwyższego poziomu!', '2026-07-18');

INSERT OR IGNORE INTO event_reviews (id, event_id, user_id, rating, review_text, review_date)
VALUES (7, 4, 4, 4, 'Ciekawa kolekcja dzieł sztuki wspócsesnej.', '2026-05-31');

INSERT OR IGNORE INTO event_reviews (id, event_id, user_id, rating, review_text, review_date)
VALUES (8, 5, 5, 5, 'Maraton organizacyjnie bez zarzutu!', '2026-08-16');

-- ========== LOCATION_CONTACTS ==========
-- Powiązanie użytkowników jako kontaktów do lokacji
INSERT OR IGNORE INTO location_contacts (location_id, user_id)
VALUES (1, 1); -- Admin to contact for Pałac Kultury

INSERT OR IGNORE INTO location_contacts (location_id, user_id)
VALUES (1, 2); -- John to contact for Pałac Kultury

INSERT OR IGNORE INTO location_contacts (location_id, user_id)
VALUES (2, 3); -- Jane to contact for Hala Widowisków

INSERT OR IGNORE INTO location_contacts (location_id, user_id)
VALUES (3, 2); -- John to contact for Stadion Energa

INSERT OR IGNORE INTO location_contacts (location_id, user_id)
VALUES (4, 4); -- Bob to contact for Ama Silesia

-- ========== SUMMARY ==========
-- Users: 5 (1 admin + 4 regular)
-- Locations: 4
-- Rooms: 7
-- Events: 5
-- Tickets: 9 (distributed among users)
-- Reviews: 8
-- Relationships maintained:
--   - Each event has locations and rooms
--   - Each ticket belongs to a user and event
--   - Each review belongs to a user and event
--   - Location contacts link users to locations

