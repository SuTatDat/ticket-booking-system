package com.ticketbooking.booking.service.impl;

import com.ticketbooking.booking.client.*;
import com.ticketbooking.booking.dto.request.*;
import com.ticketbooking.booking.dto.response.*;
import com.ticketbooking.booking.entity.Booking;
import com.ticketbooking.booking.entity.BookingSeat;
import com.ticketbooking.booking.entity.BookingStatus;
import com.ticketbooking.booking.event.publisher.BookingEventPublisher;
import com.ticketbooking.booking.mapper.BookingMapper;
import com.ticketbooking.booking.repository.*;
import com.ticketbooking.booking.service.BookingService;
import com.ticketbooking.booking.validator.BookingValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final BookingValidator bookingValidator;
    private final BookingEventPublisher eventPublisher;
    private final SeatLockServiceClient seatLockClient;
    private final InventoryServiceClient inventoryClient;

    @Override
    public BookingResponse createBooking(CreateBookingRequest request) {
        log.info("Creating booking for user {} and event {}", request.getUserId(), request.getEventId());

        // 1. Validate request
        bookingValidator.validateCreateBookingRequest(request);

        // 2. Verify seats are locked (if lockToken provided)
        if (request.getLockToken() != null) {
            try {
                // TODO: Implement when SeatLockServiceClient methods are ready
                // seatLockClient.validateLock(request.getLockToken(), request.getSeatIds());
                log.debug("Lock token validated: {}", request.getLockToken());
            } catch (Exception e) {
                log.error("Failed to validate lock token: {}", request.getLockToken(), e);
                throw new IllegalStateException("Seat lock validation failed", e);
            }
        }

        // 3. Get seat information from inventory service
        // TODO: Implement when InventoryServiceClient methods are ready
        // For now, create mock seat data
        List<BookingSeat> bookingSeats = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Long seatId : request.getSeatIds()) {
            // Mock seat data - replace with actual inventory service call
            // SeatInfo seatInfo = inventoryClient.getSeat(seatId);
            BookingSeat bookingSeat = BookingSeat.builder()
                    .seatId(seatId)
                    .seatNumber("SEAT-" + seatId) // Replace with actual seat number from inventory
                    .price(new BigDecimal("100.00")) // Replace with actual price from inventory
                    .build();
            
            bookingSeats.add(bookingSeat);
            totalAmount = totalAmount.add(bookingSeat.getPrice());
        }

        // 4. Create booking entity
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(10); // Use config value: booking.lock-duration-minutes

        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .eventId(request.getEventId())
                .status(BookingStatus.PENDING)
                .totalAmount(totalAmount)
                .expiresAt(expiresAt)
                .seats(new ArrayList<>())
                .build();

        // Set booking reference for each seat
        for (BookingSeat seat : bookingSeats) {
            seat.setBooking(booking);
            booking.getSeats().add(seat);
        }

        // 5. Save to database
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created with ID: {}", savedBooking.getId());

        // 6. Publish booking created event
        try {
            eventPublisher.publishBookingCreated(savedBooking);
        } catch (Exception e) {
            log.error("Failed to publish booking created event for booking {}", savedBooking.getId(), e);
            // Don't fail the transaction, just log the error
        }

        // 7. Convert to response
        return toBookingResponse(savedBooking);
    }

    /**
     * Helper method to convert Booking entity to BookingResponse
     */
    private BookingResponse toBookingResponse(Booking booking) {
        List<BookingResponse.SeatResponse> seatResponses = booking.getSeats().stream()
                .map(seat -> BookingResponse.SeatResponse.builder()
                        .seatId(seat.getSeatId())
                        .seatNumber(seat.getSeatNumber())
                        .price(seat.getPrice())
                        .build())
                .toList();

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .eventId(booking.getEventId())
                .userId(booking.getUserId())
                .status(booking.getStatus())
                .seats(seatResponses)
                .totalAmount(booking.getTotalAmount())
                .createdAt(booking.getCreatedAt())
                .expiresAt(booking.getExpiresAt())
                .build();
    }

    @Override
    public BookingResponse confirmBooking(ConfirmBookingRequest request) { return null; }

    @Override
    public BookingResponse cancelBooking(Long bookingId, String reason) { return null; }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBooking(Long bookingId) { return null; }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(Long userId) { return null; }

    @Override
    public void handleExpiredBookings() { }
}
