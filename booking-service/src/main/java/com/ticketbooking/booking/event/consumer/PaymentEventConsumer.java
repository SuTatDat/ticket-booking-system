package com.ticketbooking.booking.event.consumer;

import com.ticketbooking.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {
    private final BookingService bookingService;
    // TODO: @KafkaListener handlePaymentCompleted, handlePaymentFailed
}
