package com.ticketbooking.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {

    private Long id;
    private Long eventId;
    private Long ticketTypeId;
    private String section;
    private String row;
    private String seatNumber;
    private BigDecimal price;
    private String status;
}
