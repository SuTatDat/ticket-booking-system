package com.ticketbooking.booking.service;

import com.ticketbooking.booking.dto.request.*;
import com.ticketbooking.booking.dto.response.*;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request);
    BookingResponse confirmBooking(ConfirmBookingRequest request);
    BookingResponse cancelBooking(Long bookingId, String reason);
    BookingResponse getBooking(Long bookingId);
    List<BookingResponse> getUserBookings(Long userId);
    void handleExpiredBookings();
}
