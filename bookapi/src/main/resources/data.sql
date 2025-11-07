-- Drop tables if exist (opcional)
-- DROP TABLE IF EXISTS subscriptions;
-- DROP TABLE IF EXISTS users;

-- Insert test users with bcrypt hashed passwords
-- Admin password: Admin123!
-- User password: User123!

INSERT INTO users (first_name, last_name, email, password, role, phone_number, age, is_active, created_at, updated_at)
VALUES (
           'Admin',
           'System',
           'admin@letrasvivas.com',
           '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E3NgFEzFm',
           'ADMIN',
           '+50312345678',
           30,
           true,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

INSERT INTO users (first_name, last_name, email, password, role, phone_number, age, is_active, created_at, updated_at)
VALUES (
           'John',
           'Doe',
           'user@letrasvivas.com',
           '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E3NgFEzFm',
           'USER',
           '+50387654321',
           25,
           true,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );
