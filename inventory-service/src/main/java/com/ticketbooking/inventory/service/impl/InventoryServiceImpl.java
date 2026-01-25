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

    @Override
    public List<SeatResponse> getAvailableSeats(Long eventId, Long ticketTypeId) {
        log.info("Fetching available seats for event: {}, ticketTypeId: {}", eventId, ticketTypeId);

        findEventById(eventId);

        List<Seat> seats;
        if (ticketTypeId != null) {
            seats = seatRepository.findByEventIdAndTicketTypeIdAndStatus(
                    eventId, ticketTypeId, Seat.SeatStatus.AVAILABLE);
        } else {
            seats = seatRepository.findByEventIdAndStatus(eventId, Seat.SeatStatus.AVAILABLE);
        }

        return inventoryMapper.toSeatResponseList(seats);
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
