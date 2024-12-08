package com.cloudbees.booking.controller;

import com.cloudbees.booking.dto.exception.BadRequestException;
import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.model.Receipt;
import com.cloudbees.booking.model.Seat;
import com.cloudbees.booking.repository.ReceiptRepository;
import com.cloudbees.booking.service.booking.TrainBookingService;
import com.cloudbees.booking.service.passenger.PassengerService;
import com.cloudbees.booking.service.seat.SeatService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@Import(TrainBookingService.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerService passengerService;

    @MockBean
    private SeatService seatService;

    @MockBean
    private ReceiptRepository receiptRepository;

    private final static String EMAIL_ADDRESS = "abc@example.com";

    @Test
    void verifyBookingCannotBeDoneMoreThanOnce() throws Exception {
        mockGetPassengerCall();
        when(receiptRepository.saveIfAbsent(any(Receipt.class))).thenThrow(EntityExistsException.class);
        when(seatService.getVacantSeat()).thenReturn(mock(Seat.class));
        mockMvc.perform(post("/book/%s".formatted(EMAIL_ADDRESS))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"from\": \"London\", \"to\": \"France\", \"farePaid\": 20.0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyInvalidPassengerIsNotCreated() throws Exception {
        mockGetPassengerCall();
        when(receiptRepository.findByPassenger_EmailAddress(EMAIL_ADDRESS)).thenReturn(Optional.empty());
        mockMvc.perform(get("/receipt/%s".formatted(EMAIL_ADDRESS)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenChangingToAnUnavailableSeat() throws Exception {
        mockGetPassengerCall();
        String newSeatNumber = "B2";
        Receipt mockReceipt = mock(Receipt.class);
        when(receiptRepository.findByPassenger_EmailAddress(EMAIL_ADDRESS)).thenReturn(Optional.of(mockReceipt));
        when(mockReceipt.getSeat()).thenReturn(mock(Seat.class));
        when(seatService.changeSeat(any(Seat.class), eq(newSeatNumber))).thenThrow(BadRequestException.class);

        mockMvc.perform(post("/change-seat/%s?seat=%s".formatted(EMAIL_ADDRESS, newSeatNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private void mockGetPassengerCall() {
        final Passenger passenger = mock(Passenger.class);
        when(passengerService.getPassenger(EMAIL_ADDRESS)).thenReturn(passenger);
    }

}
