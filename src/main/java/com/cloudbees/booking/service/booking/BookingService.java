package com.cloudbees.booking.service.booking;

import com.cloudbees.booking.dto.Ticket;
import com.cloudbees.booking.dto.exception.BadRequestException;
import com.cloudbees.booking.model.Receipt;

public interface BookingService {

    Ticket book(Receipt receipt);

    Receipt findReceipt(String emailAddress);

    void cancelBooking(String emailAddress);

    Ticket modifySeatBooking(String emailAddress, String newSeatNumber) throws BadRequestException;
}
