package com.cloudbees.booking.service;

import com.cloudbees.booking.dto.exception.BadRequestException;
import com.cloudbees.booking.repository.SeatRepository;
import com.cloudbees.booking.service.seat.SeatService;
import com.cloudbees.booking.service.seat.TrainSeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrainSeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    private SeatService seatService;

    @BeforeEach
    void setUp() {
        this.seatService = new TrainSeatService(seatRepository);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenSectionIsInvalid() {
        String invalidSection = "Z";
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            seatService.getSeats(invalidSection);
        });
        assertEquals("Requested seats view for invalid section [Z]", thrown.getMessage());
    }

}
