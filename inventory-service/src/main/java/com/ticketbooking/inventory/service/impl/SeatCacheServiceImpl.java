package com.ticketbooking.inventory.service.impl;

import com.ticketbooking.inventory.dto.response.SeatResponse;
import com.ticketbooking.inventory.service.SeatCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of SeatCacheService using Redis.
 * Follows the caching pattern from the sequence diagram:
 * 1. getFromCache(eventId) - returns cached seats or null
 * 2. cacheSeats(eventId, seats) - stores seats in cache with TTL
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SeatCacheServiceImpl implements SeatCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${inventory.cache.seats.ttl-minutes:5}")
    private long cacheTtlMinutes;

    @Override
    public List<SeatResponse> getFromCache(Long eventId) {
        String key = buildKey(eventId);
        log.debug("Getting available seats from cache for eventId: {}", eventId);

        try {
            Object cached = redisTemplate.opsForValue().get(key);
            if (cached != null) {
                log.info("Cache HIT for available seats, eventId: {}", eventId);
                return (List<SeatResponse>) cached;
            }
            log.info("Cache MISS for available seats, eventId: {}", eventId);
            return null;
        } catch (Exception e) {
            log.error("Error getting seats from cache for eventId: {}", eventId, e);
            return null;
        }
    }

    @Override
    public List<SeatResponse> getFromCache(Long eventId, Long ticketTypeId) {
        String key = buildKey(eventId, ticketTypeId);
        log.debug("Getting available seats from cache for eventId: {}, ticketTypeId: {}", eventId, ticketTypeId);

        try {
            Object cached = redisTemplate.opsForValue().get(key);
            if (cached != null) {
                log.info("Cache HIT for available seats, eventId: {}, ticketTypeId: {}", eventId, ticketTypeId);
                return (List<SeatResponse>) cached;
            }
            log.info("Cache MISS for available seats, eventId: {}, ticketTypeId: {}", eventId, ticketTypeId);
            return null;
        } catch (Exception e) {
            log.error("Error getting seats from cache for eventId: {}, ticketTypeId: {}", eventId, ticketTypeId, e);
            return null;
        }
    }

    @Override
    public void cacheSeats(Long eventId, List<SeatResponse> seats) {
        String key = buildKey(eventId);
        log.debug("Caching {} available seats for eventId: {}", seats.size(), eventId);

        try {
            redisTemplate.opsForValue().set(key, seats, cacheTtlMinutes, TimeUnit.MINUTES);
            log.info("Successfully cached {} seats for eventId: {}, TTL: {} minutes",
                    seats.size(), eventId, cacheTtlMinutes);
        } catch (Exception e) {
            log.error("Error caching seats for eventId: {}", eventId, e);
        }
    }

    @Override
    public void cacheSeats(Long eventId, Long ticketTypeId, List<SeatResponse> seats) {
        String key = buildKey(eventId, ticketTypeId);
        log.debug("Caching {} available seats for eventId: {}, ticketTypeId: {}", seats.size(), eventId, ticketTypeId);

        try {
            redisTemplate.opsForValue().set(key, seats, cacheTtlMinutes, TimeUnit.MINUTES);
            log.info("Successfully cached {} seats for eventId: {}, ticketTypeId: {}, TTL: {} minutes",
                    seats.size(), eventId, ticketTypeId, cacheTtlMinutes);
        } catch (Exception e) {
            log.error("Error caching seats for eventId: {}, ticketTypeId: {}", eventId, ticketTypeId, e);
        }
    }

    @Override
    public void evictCache(Long eventId) {
        log.info("Evicting all seat cache for eventId: {}", eventId);

        try {
            // Evict main cache key
            String mainKey = buildKey(eventId);
            redisTemplate.delete(mainKey);

            // Evict all ticket type specific keys using pattern matching
            String pattern = AVAILABLE_SEATS_KEY_PREFIX + eventId + ":*";
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Evicted {} cache keys for eventId: {}", keys.size(), eventId);
            }
        } catch (Exception e) {
            log.error("Error evicting cache for eventId: {}", eventId, e);
        }
    }

    @Override
    public void evictCache(Long eventId, List<Long> seatIds) {
        // For simplicity, evict all cache for the event
        // In a more sophisticated implementation, you could update the cached list
        // by removing/updating specific seats
        log.info("Evicting seat cache for eventId: {} due to seat changes: {}", eventId, seatIds);
        evictCache(eventId);
    }

    /**
     * Build cache key for event-level seat availability.
     */
    private String buildKey(Long eventId) {
        return AVAILABLE_SEATS_KEY_PREFIX + eventId;
    }

    /**
     * Build cache key for ticket-type-level seat availability.
     */
    private String buildKey(Long eventId, Long ticketTypeId) {
        return AVAILABLE_SEATS_KEY_PREFIX + eventId + ":type:" + ticketTypeId;
    }
}