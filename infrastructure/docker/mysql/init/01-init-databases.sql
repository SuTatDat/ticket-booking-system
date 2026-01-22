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

-- ===========================================
-- Booking Database Tables
-- ===========================================
USE booking_db;

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_amount DECIMAL(10, 2),
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    expires_at DATETIME,
    version BIGINT DEFAULT 0,
    INDEX idx_bookings_event_id (event_id),
    INDEX idx_bookings_user_id (user_id),
    INDEX idx_bookings_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Booking seats table
CREATE TABLE IF NOT EXISTS booking_seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    seat_number VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2),
    CONSTRAINT fk_booking_seats_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    INDEX idx_booking_seats_booking_id (booking_id),
    INDEX idx_booking_seats_seat_id (seat_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
