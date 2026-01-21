package com.ticketbooking.booking.event.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String BOOKING_TOPIC = "booking-events";
    // TODO: publishBookingCreated, publishBookingConfirmed, publishBookingCancelled
}
