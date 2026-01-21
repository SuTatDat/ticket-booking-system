package com.ticketbooking.booking.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmBookingRequest {
    @NotNull
    private Long bookingId;

    @NotBlank
    private String paymentId;

    private String transactionId;
}
