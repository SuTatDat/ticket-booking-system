package com.ticketbooking.booking.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the Booking Service.
 * Converts exceptions to appropriate HTTP responses.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // TODO: Implement exception handlers
    // @ExceptionHandler(BookingNotFoundException.class) -> 404 NOT_FOUND
    // @ExceptionHandler(LockExpiredException.class) -> 410 GONE
    // @ExceptionHandler(InvalidBookingStateException.class) -> 400 BAD_REQUEST
    // @ExceptionHandler(OptimisticLockException.class) -> 409 CONFLICT
    // @ExceptionHandler(Exception.class) -> 500 INTERNAL_SERVER_ERROR
}
