package com.cloudbees.booking.service.booking;

import com.cloudbees.booking.dto.Ticket;
import com.cloudbees.booking.model.Receipt;
import com.cloudbees.booking.repository.ReceiptRepository;
import com.cloudbees.booking.service.passenger.PassengerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainBookingService implements BookingService {

    private final PassengerService passengerService;
    private final ReceiptRepository receiptRepository;

    @Override
    public Ticket book(Receipt receipt) {
        Receipt savedReceipt = receiptRepository.save(receipt);
        return new Ticket(savedReceipt, getSeat());
    }

    @Override
    public Receipt findReceipt(String emailAddress) {
        Optional<Receipt> receipt = receiptRepository.findByPassenger_EmailAddress(emailAddress);
        return receipt.orElseThrow(EntityNotFoundException::new);
    }

    private String getSeat() {
        return "5A";
    }

}
