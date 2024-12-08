package com.cloudbees.booking.controller;

import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.repository.ReceiptRepository;
import com.cloudbees.booking.service.booking.TrainBookingService;
import com.cloudbees.booking.service.passenger.PassengerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@Import(TrainBookingService.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerService passengerService;

    @MockBean
    private ReceiptRepository receiptRepository;

    @Test
    void verifyInvalidPassengerIsNotCreated() throws Exception {
        final String emailId = "abc@example.com";
        final Passenger passenger = mock(Passenger.class);
        when(passengerService.getPassenger(emailId)).thenReturn(passenger);
        when(receiptRepository.findByPassenger_EmailAddress(emailId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/receipt/%s".formatted(emailId)))
                .andExpect(status().isNotFound());
    }

}
