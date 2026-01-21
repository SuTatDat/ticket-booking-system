package com.ticketbooking.booking.service.impl;

import com.ticketbooking.booking.client.*;
import com.ticketbooking.booking.dto.request.*;
import com.ticketbooking.booking.dto.response.*;
import com.ticketbooking.booking.event.publisher.BookingEventPublisher;
import com.ticketbooking.booking.mapper.BookingMapper;
import com.ticketbooking.booking.repository.*;
import com.ticketbooking.booking.service.BookingService;
import com.ticketbooking.booking.validator.BookingValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // TODO: Implement all methods

    @Override
    public BookingResponse createBooking(CreateBookingRequest request) { return null; }

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
