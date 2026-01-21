package com.ticketbooking.booking.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign client configuration.
 * Configures retry, logging, and error handling for service-to-service calls.
 */
@Configuration
public class FeignConfig {

    // TODO: Configure
    // - Feign Logger.Level
    // - Retryer with backoff
    // - ErrorDecoder for custom error handling
    // - RequestInterceptor for auth headers
}
