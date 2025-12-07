-- Seed data for NotificationDB (Notification Service)
-- Database: NotificationDB
-- Table: notifications

-- Ensure database exists before using it
CREATE DATABASE IF NOT EXISTS NotificationDB;
USE NotificationDB;

-- Create notifications table if it doesn't exist
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message VARCHAR(1000),
    status VARCHAR(50) NOT NULL,
    `read` BOOLEAN DEFAULT FALSE,
    created_at DATETIME NOT NULL
);

-- Insert sample notifications
-- Note: userId should match existing users (1-5)
INSERT INTO notifications (user_id, type, title, message, status, `read`, created_at) VALUES
(1, 'IN_APP', 'Adoption Request Received', 'Your adoption request for Bella has been received and is under review.', 'SENT', false, NOW()),
(1, 'EMAIL', 'Adoption Approved', 'Congratulations! Your adoption request for Milo has been approved.', 'SENT', true, NOW()),
(2, 'IN_APP', 'Adoption Request Received', 'Your adoption request for Luna has been received and is under review.', 'SENT', false, NOW()),
(1, 'IN_APP', 'Adoption Approved', 'Congratulations! Your adoption request for Buddy has been approved.', 'SENT', false, NOW()),
(4, 'IN_APP', 'Adoption Request Received', 'Your adoption request for Max has been received and is under review.', 'SENT', false, NOW()),
(5, 'SMS', 'Adoption Rejected', 'We regret to inform you that your adoption request for Rocky was not approved.', 'SENT', true, NOW());

