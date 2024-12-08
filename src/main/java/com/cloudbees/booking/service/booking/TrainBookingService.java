package com.cloudbees.booking.service.booking;

import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.model.Receipt;
import com.cloudbees.booking.model.Ticket;
import com.cloudbees.booking.repository.ReceiptRepository;
import com.cloudbees.booking.service.passenger.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainBookingService implements BookingService {

    private final PassengerService passengerService;
    private final ReceiptRepository receiptRepository;

    @Override
    public Ticket book(Receipt receipt) {
        Passenger passenger = passengerService.getPassenger(receipt.getEmailAddress());
        Receipt savedReceipt = receiptRepository.save(receipt);
        return new Ticket(passenger, savedReceipt, getSeat());
    }

    private String getSeat() {
        return "5A";
    }

}
