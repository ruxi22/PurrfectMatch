-- Seed data for AdoptionDB (Adoption Service)
-- Database: AdoptionDB
-- Table: adoptions

-- Ensure database exists before using it
CREATE DATABASE IF NOT EXISTS AdoptionDB;
USE AdoptionDB;

-- Create adoptions table if it doesn't exist
CREATE TABLE IF NOT EXISTS adoptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    appointment_date_time DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- Insert sample adoption requests
-- Note: userId and petId should match existing users and pets
-- Assuming users 1-5 exist and pets 1-10 exist
INSERT INTO adoptions (user_id, pet_id, appointment_date_time, status, created_at, updated_at) VALUES
(1, 1, '2025-12-01 10:00:00', 'PENDING', NOW(), NOW()),
(2, 3, '2025-12-02 14:00:00', 'PENDING', NOW(), NOW()),
(1, 5, '2025-12-03 11:00:00', 'APPROVED', NOW(), NOW()),
(4, 2, '2025-12-04 15:00:00', 'PENDING', NOW(), NOW()),
(5, 7, '2025-12-05 09:00:00', 'REJECTED', NOW(), NOW()),
(2, 6, '2025-12-06 13:00:00', 'PENDING', NOW(), NOW()),
(1, 9, '2025-12-07 10:30:00', 'APPROVED', NOW(), NOW());

