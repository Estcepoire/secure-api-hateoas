-- Nettoyage
DELETE FROM reviews;
DELETE FROM reservations;
DELETE FROM event_categories;
DELETE FROM events;
DELETE FROM categories;
DELETE FROM users;

--Mots de passe : "1234" 
INSERT INTO users (email, password, role, name) VALUES 
('admin@test.com', '$2a$10$N9qo8uLOrickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lDB', 'ROLE_ADMIN', 'Administrateur'),
('user@test.com', '$2a$10$N9qo8uLOrickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lDB', 'ROLE_USER', 'Jean Dupont');

INSERT INTO categories (name, description) VALUES 
('Technologie', 'Conférences et ateliers sur le numérique'),
('Musique', 'Concerts et festivals musicaux'),
('Sport', 'Événements sportifs et compétitions');

INSERT INTO events (title, description, event_date, location, max_participants) VALUES 
('Java Conference 2026', 'Tout sur les nouveautés de Java 21+.', '2026-06-15 09:00:00', 'Paris, Palais des Congrès', 100),
('Jazz Summer Night', 'Une soirée inoubliable sous les étoiles.', '2026-07-20 20:30:00', 'Nice, Théâtre de Verdure', 50),
('Tournoi de Tennis', 'Compétition régionale open.', '2026-05-10 14:00:00', 'Lyon, Tennis Club', 20);

INSERT INTO event_categories (event_id, category_id) VALUES (1, 1), (2, 2), (3, 3);

INSERT INTO reservations (user_id, event_id, reservation_date, status) VALUES 
(2, 1, CURRENT_TIMESTAMP, 'CONFIRMED'),
(1, 1, CURRENT_TIMESTAMP, 'CONFIRMED'),
(2, 2, CURRENT_TIMESTAMP, 'PENDING');

INSERT INTO reviews (user_id, event_id, rating, comment, created_at) VALUES 
(2, 1, 5, 'Excellente conférence, très technique !', CURRENT_TIMESTAMP),
(1, 1, 4, 'Bonne organisation globale.', CURRENT_TIMESTAMP);


