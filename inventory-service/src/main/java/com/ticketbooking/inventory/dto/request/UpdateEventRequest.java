package com.ticketbooking.inventory.dto.request;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {

    private String name;

    private String description;

    private String venue;

    @Future(message = "Event date must be in the future")
    private LocalDateTime eventDate;

    private String status;
}
