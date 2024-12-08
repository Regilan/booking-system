package com.cloudbees.booking.controller;

import com.cloudbees.booking.dto.exception.BadRequestException;
import com.cloudbees.booking.model.Seat;
import com.cloudbees.booking.service.seat.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/seat")
    public Map<String, List<Seat>> getSeats(@RequestParam(required = false) String section) throws BadRequestException {
        return seatService.getSeats(section);
    }

}
