package com.ticketbooking.inventory.service;

import com.ticketbooking.inventory.dto.request.CreateEventRequest;
import com.ticketbooking.inventory.dto.request.UpdateEventRequest;
import com.ticketbooking.inventory.dto.response.EventResponse;
import com.ticketbooking.inventory.dto.response.SeatResponse;
import com.ticketbooking.inventory.dto.response.TicketTypeResponse;

import java.util.List;

public interface InventoryService {

    EventResponse createEvent(CreateEventRequest request);

    List<EventResponse> getAllEvents();

    EventResponse getEvent(Long id);

    EventResponse updateEvent(Long id, UpdateEventRequest request);

    void deleteEvent(Long id);

    List<TicketTypeResponse> getTicketTypes(Long eventId);

    List<SeatResponse> getAvailableSeats(Long eventId, Long ticketTypeId);

    SeatResponse getSeat(Long seatId);

    List<SeatResponse> getSeatsByIds(List<Long> ids);
}
