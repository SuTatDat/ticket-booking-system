package com.ticketbooking.booking.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "inventory-service", url = "${services.inventory.url}")
public interface InventoryServiceClient {
    // TODO: getEvent, getSeats, getSeatsByIds, updateSeatStatus
}
