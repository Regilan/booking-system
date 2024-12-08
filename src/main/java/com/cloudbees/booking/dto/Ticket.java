package com.cloudbees.booking.dto;

import com.cloudbees.booking.model.Passenger;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Ticket {

    @NotNull
    @JsonProperty("user")
    private Passenger passenger;

    @NotNull
    private ShowReceipt receipt;

    @NotBlank
    private String seatAllocated;

}
