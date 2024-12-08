package com.cloudbees.booking.handler;

import com.cloudbees.booking.model.Seat;
import com.cloudbees.booking.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatsDataHandler implements ApplicationListener<ApplicationReadyEvent> {

    public static final List<String> SECTIONS = List.of("A", "B");

    private final SeatRepository seatRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        final int seatsPerSection = 5;
        final List<Seat> seats = new LinkedList<>();
        for (String section : SECTIONS) {
            for (int index = 1; index <= seatsPerSection; index++) {
                seats.add(new Seat("%s%d".formatted(section, index)));
            }
        }
        seatRepository.saveAll(seats);
    }
}
