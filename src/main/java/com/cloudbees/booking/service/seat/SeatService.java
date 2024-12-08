package com.cloudbees.booking.service.seat;

import com.cloudbees.booking.dto.exception.BadRequestException;
import com.cloudbees.booking.model.Seat;

import java.util.List;
import java.util.Map;

public interface SeatService {

    void vacate(final Seat seat);

    Seat getVacantSeat();

    Seat getVacantSeat(final String seatNumber) throws BadRequestException;

    Map<String, List<Seat>> getSeats(String section) throws BadRequestException;

    Seat changeSeat(Seat currentSeat, String newSeatNumber) throws BadRequestException;
}
