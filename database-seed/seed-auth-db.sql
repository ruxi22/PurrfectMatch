-- Seed data for AuthDB (Auth Service)
-- Database: AuthDB
-- Table: users

-- Ensure database exists before using it
CREATE DATABASE IF NOT EXISTS AuthDB;
USE AuthDB;

-- Insert sample users
-- IMPORTANT: BCrypt hashes are unique each time they're generated
-- Use the register API endpoint instead, or generate fresh hashes
-- 
-- Option 1: Use the API to create users (RECOMMENDED):
--   POST http://localhost:8085/api/auth/register
--   Body: {"username":"john_doe","password":"password123"}
--
-- Option 2: Generate BCrypt hashes and update this script
--   You can use: https://bcrypt-generator.com/
--   Or use the generate-bcrypt-hash.java program
--
-- Option 3: Use these example hashes (may not work if BCrypt salt differs):
--   password123: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
--   admin123: $2a$10$8K1p/a0dL3XpN0K3jJ8k0e8K1p/a0dL3XpN0K3jJ8k0e8K1p/a0dL3X
--
-- For now, this script is commented out. Uncomment and update with your generated hashes:

-- INSERT INTO users (username, password, role) VALUES
-- ('john_doe', '$2a$10$YOUR_BCRYPT_HASH_HERE', 'ADOPTER'),
-- ('jane_smith', '$2a$10$YOUR_BCRYPT_HASH_HERE', 'ADOPTER'),
-- ('admin', '$2a$10$YOUR_BCRYPT_HASH_HERE', 'ADMIN'),
-- ('sarah_wilson', '$2a$10$YOUR_BCRYPT_HASH_HERE', 'ADOPTER'),
-- ('mike_jones', '$2a$10$YOUR_BCRYPT_HASH_HERE', 'ADOPTER');

