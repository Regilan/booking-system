package com.cloudbees.booking.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Ticket {

    @NotNull
    private Passenger user;

    @NotNull
    private Receipt receipt;

    @NotBlank
    private String seatAllocated;

}
