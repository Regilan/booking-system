package com.cloudbees.booking.dto;

import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.model.Receipt;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Ticket {

    @NotNull
    @JsonProperty("user")
    private Passenger passenger;

    @NotNull
    private ShowReceipt receipt;

    @NotBlank
    private String seatAllocated;

    public Ticket(final Receipt receipt, final String seatAllocated) {
        this.passenger = receipt.getPassenger();
        this.receipt = new ShowReceipt(receipt);
        this.seatAllocated = seatAllocated;
    }

}
