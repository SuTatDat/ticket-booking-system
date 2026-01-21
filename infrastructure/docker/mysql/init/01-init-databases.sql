-- ===========================================
-- Database Initialization for Docker
-- ===========================================

-- Create databases for each microservice
CREATE DATABASE IF NOT EXISTS inventory_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS booking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS payment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create application user
CREATE USER IF NOT EXISTS 'ticketuser'@'%' IDENTIFIED BY 'ticketpass';

-- Grant privileges
GRANT ALL PRIVILEGES ON inventory_db.* TO 'ticketuser'@'%';
GRANT ALL PRIVILEGES ON booking_db.* TO 'ticketuser'@'%';
GRANT ALL PRIVILEGES ON payment_db.* TO 'ticketuser'@'%';

FLUSH PRIVILEGES;
