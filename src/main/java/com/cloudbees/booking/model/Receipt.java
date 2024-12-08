package com.cloudbees.booking.model;

import com.cloudbees.booking.dto.BookingStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor
@Getter
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    @JsonProperty("from")
    private String source;

    @NotBlank
    @Column(nullable = false)
    @JsonProperty("to")
    private String destination;

    @NotNull
    @Column(nullable = false)
    private Float farePaid;

    @OneToOne
    @JoinColumn(name = "passenger_email", referencedColumnName = "emailAddress", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("user")
    private Passenger passenger;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private BookingStatus bookingStatus;

    @OneToOne
    @JoinColumn(name = "seat_id", referencedColumnName = "id")
    @Setter
    @JsonIgnore
    private Seat seat;

    public Receipt(String source, String destination, Float farePaid) {
        this.source = source;
        this.destination = destination;
        this.farePaid = farePaid;
    }

    public Receipt forPassenger(final Passenger passenger) {
        this.passenger = passenger;
        return this;
    }

    public void assignSeat(Seat seat) {
        seat.setIsAvailable(false);
        seat.setPassenger(passenger);
        seat.setReceipt(this);
        this.seat = seat;
    }
}
