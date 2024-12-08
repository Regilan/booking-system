package com.cloudbees.booking.service.seat;

import com.cloudbees.booking.dto.exception.BadRequestException;
import com.cloudbees.booking.handler.SeatsDataHandler;
import com.cloudbees.booking.model.Seat;
import com.cloudbees.booking.repository.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainSeatService implements SeatService {

    private final SeatRepository seatRepository;

    @Override
    public void vacate(Seat seat) {
        seat.vacateSeat();
        seatRepository.save(seat);
    }

    @Override
    public Seat getVacantSeat() {
        return seatRepository.fetchAnyVacantSeat();
    }

    @Override
    public Seat getVacantSeat(final String seatNumber) throws BadRequestException {
        Optional<Seat> seatOptional = seatRepository.findByNumber(seatNumber);
        if (seatOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Seat seat = seatOptional.get();
        if (Boolean.TRUE.equals(seat.getIsAvailable())) {
            return seat;
        }
        throw new BadRequestException("Requested for unavailable seat [%s]".formatted(seatNumber));
    }

    @Override
    public Map<String, List<Seat>> getSeats(String section) throws BadRequestException {
        validateSection(section);
        List<Seat> seats = getSeatsBySection(section);
        return groupSeatsBySection(seats);
    }

    @Override
    public Seat changeSeat(Seat currentSeat, String newSeatNumber) throws BadRequestException {
        if (currentSeat.getNumber().equals(newSeatNumber)) {
            throw new BadRequestException("Requested for same seat [%s]".formatted(newSeatNumber));
        }

        Seat requestedSeat = getVacantSeat(newSeatNumber);
        vacate(currentSeat);
        return requestedSeat;
    }

    private void validateSection(String section) throws BadRequestException {
        if (section != null && !SeatsDataHandler.SECTIONS.contains(section)) {
            throw new BadRequestException("Requested seats view for invalid section [%s]".formatted(section));
        }
    }

    private List<Seat> getSeatsBySection(String section) {
        if (section == null) {
            return seatRepository.findAll();
        }
        return seatRepository.findByNumberStartingWith(section);
    }

    private Map<String, List<Seat>> groupSeatsBySection(List<Seat> seats) {
        Map<String, List<Seat>> response = new HashMap<>();

        for (Seat seat : seats) {
            String currentSection = seat.getNumber().substring(0, 1);
            response.computeIfAbsent(currentSection, k -> new LinkedList<>()).add(seat);
        }

        return response;
    }
}
