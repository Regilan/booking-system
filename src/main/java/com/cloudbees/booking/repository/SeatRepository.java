package com.cloudbees.booking.repository;

import com.cloudbees.booking.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findByNumber(final String number);

    @Query(value = "SELECT * FROM Seat WHERE is_available = true ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Seat fetchAnyVacantSeat();

    List<Seat> findByNumberStartingWith(String section);
}
