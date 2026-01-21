package com.ticketbooking.booking.config;

import com.ticketbooking.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler for handling expired bookings.
 * Runs periodically to clean up PENDING bookings that have passed their expiry time.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingExpirationScheduler {

    private final BookingService bookingService;

    /**
     * Runs every minute to check for and handle expired bookings.
     */
    @Scheduled(fixedRate = 60000) // Every 1 minute
    public void handleExpiredBookings() {
        log.info("Running expired bookings cleanup job...");
        // TODO: Call bookingService.handleExpiredBookings()
    }
}
