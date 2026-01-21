package com.ticketbooking.event.booking;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Event published when a booking is confirmed.
 * Consumed by NotificationService, WebSocketService.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingConfirmedEvent {
    private Long bookingId;
    private Long userId;
    private Long eventId;
    private List<Long> seatIds;
    private BigDecimal totalAmount;
    private String transactionId;
    private LocalDateTime confirmedAt;
}
