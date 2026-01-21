package com.ticketbooking.common.enums;

/**
 * Seat status enumeration shared across services.
 */
public enum SeatStatus {
    AVAILABLE,  // Seat is available for booking
    LOCKED,     // Seat is temporarily locked (user selecting)
    BOOKED      // Seat is confirmed booked
}
