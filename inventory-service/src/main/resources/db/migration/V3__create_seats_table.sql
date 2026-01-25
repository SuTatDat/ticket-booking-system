-- Create seats table
CREATE TABLE seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    ticket_type_id BIGINT,
    section VARCHAR(50),
    seat_row VARCHAR(10),
    seat_number VARCHAR(10) NOT NULL,
    price DECIMAL(10, 2),
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',

    INDEX idx_seats_event_id (event_id),
    INDEX idx_seats_ticket_type_id (ticket_type_id),
    INDEX idx_seats_status (status),
    INDEX idx_seats_event_status (event_id, status),

    CONSTRAINT fk_seats_event
        FOREIGN KEY (event_id) REFERENCES events(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_seats_ticket_type
        FOREIGN KEY (ticket_type_id) REFERENCES ticket_types(id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
