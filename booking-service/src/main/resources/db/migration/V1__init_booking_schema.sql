-- ================================================================
-- Flyway Migration Script V1
-- Description: Initial schema for booking service
-- Author: System
-- Date: 2026-01-22
-- ================================================================

-- Create bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_amount DECIMAL(10, 2),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    expires_at DATETIME(6),
    version BIGINT DEFAULT 0,

    INDEX idx_event_id (event_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create booking_seats table
CREATE TABLE IF NOT EXISTS booking_seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    seat_number VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2),

    CONSTRAINT fk_booking_seats_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    INDEX idx_booking_id (booking_id),
    INDEX idx_seat_id (seat_id),
    INDEX idx_seat_number (seat_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add comments for documentation
ALTER TABLE bookings
    COMMENT = 'Stores ticket booking information with optimistic locking';

ALTER TABLE booking_seats
    COMMENT = 'Join table between bookings and seats';
