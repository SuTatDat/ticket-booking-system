package com.ticketbooking.inventory.service.impl;

import com.ticketbooking.inventory.dto.request.CreateEventRequest;
import com.ticketbooking.inventory.dto.request.UpdateEventRequest;
import com.ticketbooking.inventory.dto.response.EventResponse;
import com.ticketbooking.inventory.dto.response.SeatResponse;
import com.ticketbooking.inventory.dto.response.TicketTypeResponse;
import com.ticketbooking.inventory.entity.Event;
import com.ticketbooking.inventory.entity.Seat;
import com.ticketbooking.inventory.entity.TicketType;
import com.ticketbooking.common.exception.ResourceNotFoundException;
import com.ticketbooking.inventory.mapper.InventoryMapper;
import com.ticketbooking.inventory.repository.EventRepository;
import com.ticketbooking.inventory.repository.SeatRepository;
import com.ticketbooking.inventory.repository.TicketTypeRepository;
import com.ticketbooking.inventory.service.InventoryService;
import com.ticketbooking.inventory.service.SeatCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final SeatRepository seatRepository;
    private final InventoryMapper inventoryMapper;
    private final SeatCacheService seatCacheService;

    @Override
    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        log.info("Creating event: {}", request.getName());

        Event event = inventoryMapper.toEvent(request);
        Event savedEvent = eventRepository.save(event);

        log.info("Event created with id: {}", savedEvent.getId());
        return inventoryMapper.toEventResponse(savedEvent);
    }

    @Override
    public List<EventResponse> getAllEvents() {
        log.info("Fetching all events");
        List<Event> events = eventRepository.findAll();
        return inventoryMapper.toEventResponseList(events);
    }

    @Override
    public EventResponse getEvent(Long id) {
        log.info("Fetching event with id: {}", id);
        Event event = findEventById(id);
        return inventoryMapper.toEventResponse(event);
    }

    @Override
    @Transactional
    public EventResponse updateEvent(Long id, UpdateEventRequest request) {
        log.info("Updating event with id: {}", id);

        Event event = findEventById(id);

        if (request.getName() != null) {
            event.setName(request.getName());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getVenue() != null) {
            event.setVenue(request.getVenue());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getStatus() != null) {
            event.setStatus(Event.EventStatus.valueOf(request.getStatus()));
        }

        Event updatedEvent = eventRepository.save(event);
        log.info("Event updated: {}", updatedEvent.getId());
        return inventoryMapper.toEventResponse(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        log.info("Deleting event with id: {}", id);

        Event event = findEventById(id);
        eventRepository.delete(event);

        log.info("Event deleted: {}", id);
    }

    @Override
    public List<TicketTypeResponse> getTicketTypes(Long eventId) {
        log.info("Fetching ticket types for event: {}", eventId);

        findEventById(eventId);
        List<TicketType> ticketTypes = ticketTypeRepository.findByEventId(eventId);

        return inventoryMapper.toTicketTypeResponseList(ticketTypes);
    }

    /**
     * Get available seats for an event with Redis caching.
     *
     * Flow (as per sequence diagram - Phase 1: View Available Seats):
     * 1. getFromCache(eventId) - Try to get from Redis cache
     * 2. If Cache Hit - Return cached seats immediately
     * 3. If Cache Miss - Query MySQL: findAvailableByEventId(eventId)
     * 4. cacheSeats(eventId, seats) - Store result in Redis
     * 5. Return seats with availability info
     */
    @Override
    public List<SeatResponse> getAvailableSeats(Long eventId, Long ticketTypeId) {
        log.info("Fetching available seats for event: {}, ticketTypeId: {}", eventId, ticketTypeId);

        // Validate event exists
        findEventById(eventId);

        // Step 1: Try to get from cache
        List<SeatResponse> cachedSeats;
        if (ticketTypeId != null) {
            cachedSeats = seatCacheService.getFromCache(eventId, ticketTypeId);
        } else {
            cachedSeats = seatCacheService.getFromCache(eventId);
        }

        // Step 2: Cache Hit - return cached seats
        if (cachedSeats != null) {
            log.debug("Returning {} cached seats for eventId: {}", cachedSeats.size(), eventId);
            return cachedSeats;
        }

        // Step 3: Cache Miss - query database
        log.debug("Cache miss, querying database for eventId: {}", eventId);
        List<Seat> seats;
        if (ticketTypeId != null) {
            seats = seatRepository.findByEventIdAndTicketTypeIdAndStatus(
                    eventId, ticketTypeId, Seat.SeatStatus.AVAILABLE);
        } else {
            seats = seatRepository.findByEventIdAndStatus(eventId, Seat.SeatStatus.AVAILABLE);
        }

        // Convert to response
        List<SeatResponse> seatResponses = inventoryMapper.toSeatResponseList(seats);

        // Step 4: Cache the result
        if (ticketTypeId != null) {
            seatCacheService.cacheSeats(eventId, ticketTypeId, seatResponses);
        } else {
            seatCacheService.cacheSeats(eventId, seatResponses);
        }

        // Step 5: Return seats (with availability)
        log.debug("Returning {} seats from database for eventId: {}", seatResponses.size(), eventId);
        return seatResponses;
    }


    @Override
    public SeatResponse getSeat(Long seatId) {
        log.info("Fetching seat with id: {}", seatId);
        Seat seat = findSeatById(seatId);
        return inventoryMapper.toSeatResponse(seat);
    }

    @Override
    public List<SeatResponse> getSeatsByIds(List<Long> ids) {
        log.info("Fetching seats by ids: {}", ids);
        List<Seat> seats = seatRepository.findByIdIn(ids);
        return inventoryMapper.toSeatResponseList(seats);
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", id));
    }

    private Seat findSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", id));
    }
}
