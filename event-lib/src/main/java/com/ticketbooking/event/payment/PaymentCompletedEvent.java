package com.ticketbooking.event.payment;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a payment is completed successfully.
 * Consumed by BookingService, NotificationService.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCompletedEvent {
    private Long paymentId;
    private Long bookingId;
    private BigDecimal amount;
    private String transactionId;
    private String paymentMethod;
    private LocalDateTime completedAt;
}
