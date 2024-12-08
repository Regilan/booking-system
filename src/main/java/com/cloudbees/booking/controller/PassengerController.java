package com.cloudbees.booking.controller;

import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.service.passenger.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewPassenger(@RequestBody @Valid final Passenger passenger) {
        passengerService.createNewPassenger(passenger);
    }

    @GetMapping("/{emailAddress}")
    @ResponseStatus(HttpStatus.OK)
    public Passenger fetchPassenger(@PathVariable final String emailAddress) {
        return passengerService.getPassenger(emailAddress);
    }

}
