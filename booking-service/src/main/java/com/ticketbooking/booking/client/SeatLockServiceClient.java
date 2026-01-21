package com.ticketbooking.booking.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "seat-lock-service", url = "${services.seat-lock.url}")
public interface SeatLockServiceClient {
    // TODO: lockSeats, unlockSeats, extendLock, validateLock
}
