package com.ticketbooking.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * BookingSeat entity - join table between Booking and Seat.
 */
@Entity
@Table(name = "booking_seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private Long seatId;

    @Column(nullable = false)
    private String seatNumber;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;
}
