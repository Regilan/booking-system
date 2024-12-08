package com.cloudbees.booking.controller;

import com.cloudbees.booking.dto.Ticket;
import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.model.Receipt;
import com.cloudbees.booking.service.booking.BookingService;
import com.cloudbees.booking.service.passenger.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
public class BookingController {

    private final PassengerService passengerService;
    private final BookingService bookingService;

    @PostMapping("/book/{emailAddress}")
    public Ticket bookTicket(@PathVariable final String emailAddress, @RequestBody @Valid final Receipt receipt) {
        Passenger passenger = passengerService.getPassenger(emailAddress);
        return bookingService.book(receipt.forPassenger(passenger));
    }

    @GetMapping("/receipt/{emailAddress}")
    public Receipt getReceipt(@PathVariable final String emailAddress) {
        return bookingService.findReceipt(emailAddress);
    }

    @PostMapping("/cancel-booking/{emailAddress}")
    public Receipt cancelBooking(@PathVariable final String emailAddress) {
        return bookingService.cancelBooking(emailAddress);
    }
}
