package com.ticketbooking.booking.exception;

public class LockExpiredException extends RuntimeException {
    public LockExpiredException(String lockToken) {
        super("Lock has expired: " + lockToken);
    }
}
