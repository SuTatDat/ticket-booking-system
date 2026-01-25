package com.ticketbooking.inventory.service.impl;

import com.ticketbooking.common.exception.ResourceNotFoundException;
import com.ticketbooking.inventory.dto.request.CreateEventRequest;
import com.ticketbooking.inventory.dto.request.UpdateEventRequest;
import com.ticketbooking.inventory.dto.response.EventResponse;
import com.ticketbooking.inventory.dto.response.SeatResponse;
import com.ticketbooking.inventory.dto.response.TicketTypeResponse;
import com.ticketbooking.inventory.entity.Event;
import com.ticketbooking.inventory.entity.Seat;
import com.ticketbooking.inventory.entity.TicketType;
import com.ticketbooking.inventory.mapper.InventoryMapper;
import com.ticketbooking.inventory.repository.EventRepository;
import com.ticketbooking.inventory.repository.SeatRepository;
import com.ticketbooking.inventory.repository.TicketTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Event event;
    private EventResponse eventResponse;
    private CreateEventRequest createEventRequest;
    private UpdateEventRequest updateEventRequest;
    private TicketType ticketType;
    private TicketTypeResponse ticketTypeResponse;
    private Seat seat;
    private SeatResponse seatResponse;

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .id(1L)
                .name("Concert")
                .description("A great concert")
                .venue("Stadium")
                .eventDate(LocalDateTime.now().plusDays(30))
                .totalSeats(1000)
                .availableSeats(1000)
                .status(Event.EventStatus.ACTIVE)
                .build();

        eventResponse = EventResponse.builder()
                .id(1L)
                .name("Concert")
                .description("A great concert")
                .venue("Stadium")
                .eventDate(event.getEventDate())
                .totalSeats(1000)
                .availableSeats(1000)
                .status("ACTIVE")
                .build();

        createEventRequest = CreateEventRequest.builder()
                .name("Concert")
                .description("A great concert")
                .venue("Stadium")
                .eventDate(LocalDateTime.now().plusDays(30))
                .totalSeats(1000)
                .build();

        updateEventRequest = UpdateEventRequest.builder()
                .name("Updated Concert")
                .venue("New Stadium")
                .build();

        ticketType = TicketType.builder()
                .id(1L)
                .event(event)
                .name("VIP")
                .price(BigDecimal.valueOf(100))
                .totalQuantity(100)
                .availableQuantity(100)
                .build();

        ticketTypeResponse = TicketTypeResponse.builder()
                .id(1L)
                .eventId(1L)
                .name("VIP")
                .price(BigDecimal.valueOf(100))
                .totalQuantity(100)
                .availableQuantity(100)
                .build();

        seat = Seat.builder()
                .id(1L)
                .event(event)
                .ticketType(ticketType)
                .section("A")
                .row("1")
                .seatNumber("1")
                .price(BigDecimal.valueOf(100))
                .status(Seat.SeatStatus.AVAILABLE)
                .build();

        seatResponse = SeatResponse.builder()
                .id(1L)
                .eventId(1L)
                .ticketTypeId(1L)
                .section("A")
                .row("1")
                .seatNumber("1")
                .price(BigDecimal.valueOf(100))
                .status("AVAILABLE")
                .build();
    }

    @Nested
    @DisplayName("createEvent")
    class CreateEventTests {

        @Test
        @DisplayName("should create event successfully")
        void shouldCreateEventSuccessfully() {
            when(inventoryMapper.toEvent(createEventRequest)).thenReturn(event);
            when(eventRepository.save(event)).thenReturn(event);
            when(inventoryMapper.toEventResponse(event)).thenReturn(eventResponse);

            EventResponse result = inventoryService.createEvent(createEventRequest);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Concert");
            assertThat(result.getVenue()).isEqualTo("Stadium");

            verify(inventoryMapper).toEvent(createEventRequest);
            verify(eventRepository).save(event);
            verify(inventoryMapper).toEventResponse(event);
        }
    }

    @Nested
    @DisplayName("getAllEvents")
    class GetAllEventsTests {

        @Test
        @DisplayName("should return all events")
        void shouldReturnAllEvents() {
            List<Event> events = Arrays.asList(event);
            List<EventResponse> eventResponses = Arrays.asList(eventResponse);

            when(eventRepository.findAll()).thenReturn(events);
            when(inventoryMapper.toEventResponseList(events)).thenReturn(eventResponses);

            List<EventResponse> result = inventoryService.getAllEvents();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Concert");

            verify(eventRepository).findAll();
            verify(inventoryMapper).toEventResponseList(events);
        }

        @Test
        @DisplayName("should return empty list when no events")
        void shouldReturnEmptyListWhenNoEvents() {
            when(eventRepository.findAll()).thenReturn(Collections.emptyList());
            when(inventoryMapper.toEventResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

            List<EventResponse> result = inventoryService.getAllEvents();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getEvent")
    class GetEventTests {

        @Test
        @DisplayName("should return event when found")
        void shouldReturnEventWhenFound() {
            when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
            when(inventoryMapper.toEventResponse(event)).thenReturn(eventResponse);

            EventResponse result = inventoryService.getEvent(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Concert");

            verify(eventRepository).findById(1L);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when event not found")
        void shouldThrowExceptionWhenEventNotFound() {
            when(eventRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> inventoryService.getEvent(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Event")
                    .hasMessageContaining("999");

            verify(eventRepository).findById(999L);
            verify(inventoryMapper, never()).toEventResponse(any());
        }
    }

    @Nested
    @DisplayName("updateEvent")
    class UpdateEventTests {

        @Test
        @DisplayName("should update event successfully")
        void shouldUpdateEventSuccessfully() {
            Event updatedEvent = Event.builder()
                    .id(1L)
                    .name("Updated Concert")
                    .venue("New Stadium")
                    .build();

            EventResponse updatedResponse = EventResponse.builder()
                    .id(1L)
                    .name("Updated Concert")
                    .venue("New Stadium")
                    .build();

            when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
            when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);
            when(inventoryMapper.toEventResponse(any(Event.class))).thenReturn(updatedResponse);

            EventResponse result = inventoryService.updateEvent(1L, updateEventRequest);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Updated Concert");
            assertThat(result.getVenue()).isEqualTo("New Stadium");

            verify(eventRepository).findById(1L);
            verify(eventRepository).save(any(Event.class));
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when updating non-existent event")
        void shouldThrowExceptionWhenUpdatingNonExistentEvent() {
            when(eventRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> inventoryService.updateEvent(999L, updateEventRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Event");

            verify(eventRepository).findById(999L);
            verify(eventRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deleteEvent")
    class DeleteEventTests {

        @Test
        @DisplayName("should delete event successfully")
        void shouldDeleteEventSuccessfully() {
            when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
            doNothing().when(eventRepository).delete(event);

            inventoryService.deleteEvent(1L);

            verify(eventRepository).findById(1L);
            verify(eventRepository).delete(event);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when deleting non-existent event")
        void shouldThrowExceptionWhenDeletingNonExistentEvent() {
            when(eventRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> inventoryService.deleteEvent(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Event");

            verify(eventRepository).findById(999L);
            verify(eventRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("getTicketTypes")
    class GetTicketTypesTests {

        @Test
        @DisplayName("should return ticket types for event")
        void shouldReturnTicketTypesForEvent() {
            List<TicketType> ticketTypes = Arrays.asList(ticketType);
            List<TicketTypeResponse> ticketTypeResponses = Arrays.asList(ticketTypeResponse);

            when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
            when(ticketTypeRepository.findByEventId(1L)).thenReturn(ticketTypes);
            when(inventoryMapper.toTicketTypeResponseList(ticketTypes)).thenReturn(ticketTypeResponses);

            List<TicketTypeResponse> result = inventoryService.getTicketTypes(1L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("VIP");

            verify(eventRepository).findById(1L);
            verify(ticketTypeRepository).findByEventId(1L);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when event not found")
        void shouldThrowExceptionWhenEventNotFound() {
            when(eventRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> inventoryService.getTicketTypes(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Event");

            verify(ticketTypeRepository, never()).findByEventId(any());
        }
    }

    @Nested
    @DisplayName("getAvailableSeats")
    class GetAvailableSeatsTests {

        @Test
        @DisplayName("should return available seats for event")
        void shouldReturnAvailableSeatsForEvent() {
            List<Seat> seats = Arrays.asList(seat);
            List<SeatResponse> seatResponses = Arrays.asList(seatResponse);

            when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
            when(seatRepository.findByEventIdAndStatus(1L, Seat.SeatStatus.AVAILABLE)).thenReturn(seats);
            when(inventoryMapper.toSeatResponseList(seats)).thenReturn(seatResponses);

            List<SeatResponse> result = inventoryService.getAvailableSeats(1L, null);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo("AVAILABLE");

            verify(seatRepository).findByEventIdAndStatus(1L, Seat.SeatStatus.AVAILABLE);
        }

        @Test
        @DisplayName("should return available seats filtered by ticket type")
        void shouldReturnAvailableSeatsFilteredByTicketType() {
            List<Seat> seats = Arrays.asList(seat);
            List<SeatResponse> seatResponses = Arrays.asList(seatResponse);

            when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
            when(seatRepository.findByEventIdAndTicketTypeIdAndStatus(1L, 1L, Seat.SeatStatus.AVAILABLE))
                    .thenReturn(seats);
            when(inventoryMapper.toSeatResponseList(seats)).thenReturn(seatResponses);

            List<SeatResponse> result = inventoryService.getAvailableSeats(1L, 1L);

            assertThat(result).hasSize(1);

            verify(seatRepository).findByEventIdAndTicketTypeIdAndStatus(1L, 1L, Seat.SeatStatus.AVAILABLE);
        }
    }

    @Nested
    @DisplayName("getSeat")
    class GetSeatTests {

        @Test
        @DisplayName("should return seat when found")
        void shouldReturnSeatWhenFound() {
            when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
            when(inventoryMapper.toSeatResponse(seat)).thenReturn(seatResponse);

            SeatResponse result = inventoryService.getSeat(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getSeatNumber()).isEqualTo("1");

            verify(seatRepository).findById(1L);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when seat not found")
        void shouldThrowExceptionWhenSeatNotFound() {
            when(seatRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> inventoryService.getSeat(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Seat");

            verify(seatRepository).findById(999L);
        }
    }

    @Nested
    @DisplayName("getSeatsByIds")
    class GetSeatsByIdsTests {

        @Test
        @DisplayName("should return seats by ids")
        void shouldReturnSeatsByIds() {
            List<Long> ids = Arrays.asList(1L, 2L);
            List<Seat> seats = Arrays.asList(seat);
            List<SeatResponse> seatResponses = Arrays.asList(seatResponse);

            when(seatRepository.findByIdIn(ids)).thenReturn(seats);
            when(inventoryMapper.toSeatResponseList(seats)).thenReturn(seatResponses);

            List<SeatResponse> result = inventoryService.getSeatsByIds(ids);

            assertThat(result).hasSize(1);

            verify(seatRepository).findByIdIn(ids);
            verify(inventoryMapper).toSeatResponseList(seats);
        }

        @Test
        @DisplayName("should return empty list when no seats found")
        void shouldReturnEmptyListWhenNoSeatsFound() {
            List<Long> ids = Arrays.asList(999L);

            when(seatRepository.findByIdIn(ids)).thenReturn(Collections.emptyList());
            when(inventoryMapper.toSeatResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

            List<SeatResponse> result = inventoryService.getSeatsByIds(ids);

            assertThat(result).isEmpty();
        }
    }
}
