-- Create all databases for Purrfect Match microservices
-- This script runs automatically when the MariaDB container starts

CREATE DATABASE IF NOT EXISTS AuthDB;
CREATE DATABASE IF NOT EXISTS PetDB;
CREATE DATABASE IF NOT EXISTS AdoptionDB;
CREATE DATABASE IF NOT EXISTS NotificationDB;

-- Grant all privileges to root user
GRANT ALL PRIVILEGES ON AuthDB.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON PetDB.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON AdoptionDB.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON NotificationDB.* TO 'root'@'%';

FLUSH PRIVILEGES;


