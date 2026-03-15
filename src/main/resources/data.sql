-- H2 : création automatique des tables par JPA
-- On insère juste les données

-- Table USERS (créée automatiquement par @Entity)
INSERT INTO users (email, password, role) VALUES ('admin@test.com', '1234', 'ROLE_ADMIN');
INSERT INTO users (email, password, role) VALUES ('user@test.com', '1234', 'ROLE_USER');

-- Table PRODUCTS (créée automatiquement par @Entity)
-- Optionnel : vous pouvez pré-remplir
-- INSERT INTO products (name, price) VALUES ('Demo Product', 99.99);