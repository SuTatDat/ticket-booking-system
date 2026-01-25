package com.ticketbooking.inventory.service;

import com.ticketbooking.inventory.dto.response.SeatResponse;

import java.util.List;

public interface SeatCacheService {
    List<SeatResponse> getFromCache(Long eventId, Long ticketTypeId);

    List<SeatResponse> getFromCache(Long eventId);

    void cacheSeats(Long eventId, Long ticketTypeId, List<SeatResponse> seatResponses);

    void cacheSeats(Long eventId, List<SeatResponse> seatResponses);
}
