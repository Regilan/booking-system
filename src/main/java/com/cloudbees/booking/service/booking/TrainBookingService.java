package com.cloudbees.booking.service.booking;

import com.cloudbees.booking.dto.ShowReceipt;
import com.cloudbees.booking.dto.Ticket;
import com.cloudbees.booking.dto.exception.BadRequestException;
import com.cloudbees.booking.model.Receipt;
import com.cloudbees.booking.model.Seat;
import com.cloudbees.booking.repository.ReceiptRepository;
import com.cloudbees.booking.service.seat.SeatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainBookingService implements BookingService {

    private final SeatService seatService;
    private final ReceiptRepository receiptRepository;

    @Override
    public Ticket book(Receipt receipt) {
        Seat seat = seatService.getVacantSeat();
        receipt.assignSeat(seat);

        Receipt savedReceipt = receiptRepository.saveIfAbsent(receipt);
        return new Ticket(savedReceipt.getPassenger(), new ShowReceipt(receipt), seat.getNumber());
    }

    @Override
    public Receipt findReceipt(String emailAddress) {
        Optional<Receipt> receipt = receiptRepository.findByPassenger_EmailAddress(emailAddress);
        return receipt.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void cancelBooking(String emailAddress) {
        Receipt receipt = findReceipt(emailAddress);
        seatService.vacate(receipt.getSeat());
        receiptRepository.delete(receipt);
    }

    @Override
    public Ticket modifySeatBooking(String emailAddress, String newSeatNumber) throws BadRequestException {
        Receipt receipt = findReceipt(emailAddress);
        Seat currentSeat = receipt.getSeat();

        Seat requestedSeat = seatService.changeSeat(currentSeat, newSeatNumber);
        receipt.assignSeat(requestedSeat);

        receiptRepository.save(receipt);
        return new Ticket(receipt.getPassenger(), new ShowReceipt(receipt), newSeatNumber);
    }

}
