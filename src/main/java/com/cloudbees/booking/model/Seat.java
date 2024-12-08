package com.cloudbees.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Getter
    private String number;

    @NotNull
    @Column(nullable = false)
    @Setter
    @Getter
    private Boolean isAvailable = true;

    @OneToOne
    @JoinColumn(name = "passenger_email", referencedColumnName = "emailAddress", unique = true)
    @Setter
    @Getter
    private Passenger passenger;

    @OneToOne(mappedBy = "seat")
    @Setter
    private Receipt receipt;

    public Seat(String number) {
        this.number = number;
    }

    public void vacateSeat() {
        this.passenger = null;
        this.receipt.setSeat(null);
        isAvailable = true;
    }

}
