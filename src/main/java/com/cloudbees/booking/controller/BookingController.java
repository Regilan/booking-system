package com.cloudbees.booking.controller;

import com.cloudbees.booking.model.Receipt;
import com.cloudbees.booking.model.Ticket;
import com.cloudbees.booking.service.booking.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/book")
    @ResponseStatus(HttpStatus.OK)
    public Ticket bookTicket(@RequestBody @Valid final Receipt receipt) {
        return bookingService.book(receipt);
    }

}
