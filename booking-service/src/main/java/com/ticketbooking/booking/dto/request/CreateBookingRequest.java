package com.ticketbooking.booking.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long eventId;

    @NotEmpty
    private List<Long> seatIds;

    private String lockToken;
}
