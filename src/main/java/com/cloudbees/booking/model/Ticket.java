package com.cloudbees.booking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Ticket {

    @NotNull
    @JsonProperty("user")
    private Passenger passenger;

    @NotNull
    private Receipt receipt;

    @NotBlank
    private String seatAllocated;

}
