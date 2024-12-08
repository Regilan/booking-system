package com.cloudbees.booking.dto;

import com.cloudbees.booking.model.Receipt;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShowReceipt {

    private Long id;

    @NotBlank
    @JsonProperty("from")
    private String source;

    @NotBlank
    @JsonProperty("to")
    private String destination;

    @NotNull
    private Float farePaid;

    @NotNull
    private BookingStatus status;

    public ShowReceipt(final Receipt receipt) {
        this.id = receipt.getId();
        this.source = receipt.getSource();
        this.destination = receipt.getDestination();
        this.farePaid = receipt.getFarePaid();
        this.status = receipt.getBookingStatus();
    }

}
