package com.cloudbees.booking.controller;

import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.repository.PassengerRepository;
import com.cloudbees.booking.service.passenger.PassengerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PassengerController.class)
@Import(PassengerServiceImpl.class)
class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerRepository passengerRepository;

    @Test
    void verifyInvalidPassengerIsNotCreated() throws Exception {
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"emailAddress\": \"\", \"firstname\": \"\", \"lastName\": \"\"}"))
                .andExpect(status().isBadRequest());
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    void shouldFetchExistingPassenger() throws Exception {
        final String emailId = "abc@example.com";
        final Passenger passenger = new Passenger(emailId, "Abc", "Xyz");
        when(passengerRepository.findById(emailId)).thenReturn(Optional.of(passenger));

        mockMvc.perform(get("/user/%s".formatted(emailId)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.emailAddress").value(emailId),
                        jsonPath("$.firstName").value("Abc"),
                        jsonPath("$.lastName").value("Xyz")
                );
    }

    @Test
    void verifyNonExistingUserCannotBeFetched() throws Exception {
        final String invalidEmailId = "invalid.passenger@example.com";
        when(passengerRepository.findById(invalidEmailId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/user/%s".formatted(invalidEmailId)))
                .andExpect(status().isNotFound());
    }

}
