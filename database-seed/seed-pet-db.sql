-- Seed data for PetDB (Pet Service)
-- Database: PetDB
-- Table: pets

-- Ensure database exists before using it
CREATE DATABASE IF NOT EXISTS PetDB;
USE PetDB;

-- Create pets table if it doesn't exist
CREATE TABLE IF NOT EXISTS pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    breed VARCHAR(255),
    age DOUBLE,
    gender VARCHAR(50),
    health VARCHAR(255),
    personality VARCHAR(500),
    photo_path VARCHAR(500),
    status VARCHAR(50)
);

-- Insert sample pets
INSERT INTO pets (name, breed, age, gender, health, personality, photo_path, status) VALUES
('Bella', 'Golden Retriever', 2.5, 'Female', 'Healthy', 'Friendly and energetic', NULL, 'AVAILABLE'),
('Max', 'German Shepherd', 3.0, 'Male', 'Healthy', 'Loyal and protective', NULL, 'AVAILABLE'),
('Luna', 'Persian Cat', 1.5, 'Female', 'Healthy', 'Calm and affectionate', NULL, 'AVAILABLE'),
('Charlie', 'Labrador Retriever', 4.0, 'Male', 'Healthy', 'Playful and social', NULL, 'AVAILABLE'),
('Milo', 'Siamese Cat', 2.0, 'Male', 'Healthy', 'Curious and vocal', NULL, 'AVAILABLE'),
('Daisy', 'Beagle', 1.0, 'Female', 'Healthy', 'Happy and friendly', NULL, 'AVAILABLE'),
('Rocky', 'Bulldog', 5.0, 'Male', 'Good', 'Gentle and calm', NULL, 'AVAILABLE'),
('Sophie', 'Maine Coon', 3.5, 'Female', 'Healthy', 'Independent and playful', NULL, 'AVAILABLE'),
('Buddy', 'Border Collie', 2.0, 'Male', 'Healthy', 'Intelligent and active', NULL, 'AVAILABLE'),
('Chloe', 'Ragdoll Cat', 1.0, 'Female', 'Healthy', 'Docile and gentle', NULL, 'AVAILABLE');

