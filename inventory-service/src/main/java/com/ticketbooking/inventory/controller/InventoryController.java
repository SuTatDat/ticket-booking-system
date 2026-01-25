package com.ticketbooking.inventory.controller;

import com.ticketbooking.inventory.dto.request.CreateEventRequest;
import com.ticketbooking.inventory.dto.request.UpdateEventRequest;
import com.ticketbooking.inventory.dto.response.EventResponse;
import com.ticketbooking.inventory.dto.response.SeatResponse;
import com.ticketbooking.inventory.dto.response.TicketTypeResponse;
import com.ticketbooking.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory management APIs")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/events")
    @Operation(summary = "Create a new event")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createEvent(request));
    }

    @GetMapping("/events")
    @Operation(summary = "Get all events")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(inventoryService.getAllEvents());
    }

    @GetMapping("/events/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getEvent(id));
    }

    @PutMapping("/events/{id}")
    @Operation(summary = "Update an event")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @Valid @RequestBody UpdateEventRequest request) {
        return ResponseEntity.ok(inventoryService.updateEvent(id, request));
    }

    @DeleteMapping("/events/{id}")
    @Operation(summary = "Delete an event")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        inventoryService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/events/{eventId}/ticket-types")
    @Operation(summary = "Get ticket types for an event")
    public ResponseEntity<List<TicketTypeResponse>> getTicketTypes(@PathVariable Long eventId) {
        return ResponseEntity.ok(inventoryService.getTicketTypes(eventId));
    }

    @GetMapping("/events/{eventId}/seats")
    @Operation(summary = "Get available seats for an event")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(
            @PathVariable Long eventId,
            @RequestParam(required = false) Long ticketTypeId) {
        return ResponseEntity.ok(inventoryService.getAvailableSeats(eventId, ticketTypeId));
    }

    @GetMapping("/seats/{seatId}")
    @Operation(summary = "Get seat by ID")
    public ResponseEntity<SeatResponse> getSeat(@PathVariable Long seatId) {
        return ResponseEntity.ok(inventoryService.getSeat(seatId));
    }

    @GetMapping("/seats")
    @Operation(summary = "Get seats by IDs")
    public ResponseEntity<List<SeatResponse>> getSeatsByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(inventoryService.getSeatsByIds(ids));
    }
}
