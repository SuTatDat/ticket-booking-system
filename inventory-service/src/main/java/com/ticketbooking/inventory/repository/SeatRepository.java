package com.ticketbooking.inventory.repository;

import com.ticketbooking.inventory.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByEventIdAndStatus(Long eventId, Seat.SeatStatus status);

    List<Seat> findByEventIdAndTicketTypeIdAndStatus(Long eventId, Long ticketTypeId, Seat.SeatStatus status);

    List<Seat> findByIdIn(List<Long> ids);
}
