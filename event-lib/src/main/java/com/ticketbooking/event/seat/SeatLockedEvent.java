package com.ticketbooking.event.seat;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Event published when seats are locked.
 * Consumed by WebSocketService for real-time updates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatLockedEvent {
    private Long eventId;
    private List<Long> seatIds;
    private Long userId;
    private String lockToken;
    private LocalDateTime lockedAt;
    private LocalDateTime expiresAt;
}
