package com.ticketbooking.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private Long id;
    private String name;
    private String description;
    private String venue;
    private LocalDateTime eventDate;
    private Integer totalSeats;
    private Integer availableSeats;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
