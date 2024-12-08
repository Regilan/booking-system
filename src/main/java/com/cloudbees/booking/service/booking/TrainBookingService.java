package com.cloudbees.booking.service.booking;

import com.cloudbees.booking.dto.BookingStatus;
import com.cloudbees.booking.dto.ShowReceipt;
import com.cloudbees.booking.dto.Ticket;
import com.cloudbees.booking.model.Receipt;
import com.cloudbees.booking.repository.ReceiptRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainBookingService implements BookingService {

    private final ReceiptRepository receiptRepository;

    @Override
    public Ticket book(Receipt receipt) {
        receipt.setBookingStatus(BookingStatus.CONFIRMED);
        Receipt savedReceipt = receiptRepository.saveIfAbsent(receipt);
        return new Ticket(savedReceipt.getPassenger(), new ShowReceipt(receipt), getSeat());
    }

    @Override
    public Receipt findReceipt(String emailAddress) {
        Optional<Receipt> receipt = receiptRepository.findByPassenger_EmailAddress(emailAddress);
        return receipt.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Receipt cancelBooking(String emailAddress) {
        Receipt receipt = findReceipt(emailAddress);
        receipt.setBookingStatus(BookingStatus.CANCELLED);
        return receiptRepository.save(receipt);
    }

    private String getSeat() {
        return "5A";
    }

}
