package com.ticketbooking.booking.dto.response;

import com.ticketbooking.booking.entity.BookingStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long bookingId;
    private Long eventId;
    private Long userId;
    private BookingStatus status;
    private List<SeatResponse> seats;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SeatResponse {
        private Long seatId;
        private String seatNumber;
        private BigDecimal price;
    }
}
