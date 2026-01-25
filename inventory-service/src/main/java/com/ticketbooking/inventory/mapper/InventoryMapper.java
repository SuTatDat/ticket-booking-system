package com.ticketbooking.inventory.mapper;

import com.ticketbooking.inventory.dto.request.CreateEventRequest;
import com.ticketbooking.inventory.dto.response.EventResponse;
import com.ticketbooking.inventory.dto.response.SeatResponse;
import com.ticketbooking.inventory.dto.response.TicketTypeResponse;
import com.ticketbooking.inventory.entity.Event;
import com.ticketbooking.inventory.entity.Seat;
import com.ticketbooking.inventory.entity.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableSeats", source = "totalSeats")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "ticketTypes", ignore = true)
    @Mapping(target = "seats", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Event toEvent(CreateEventRequest request);

    @Mapping(target = "status", expression = "java(event.getStatus().name())")
    EventResponse toEventResponse(Event event);

    List<EventResponse> toEventResponseList(List<Event> events);

    @Mapping(target = "eventId", source = "event.id")
    TicketTypeResponse toTicketTypeResponse(TicketType ticketType);

    List<TicketTypeResponse> toTicketTypeResponseList(List<TicketType> ticketTypes);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "ticketTypeId", source = "ticketType.id")
    @Mapping(target = "status", expression = "java(seat.getStatus().name())")
    SeatResponse toSeatResponse(Seat seat);

    List<SeatResponse> toSeatResponseList(List<Seat> seats);
}
