package com.cloudbees.booking.service.booking;

import com.cloudbees.booking.dto.Ticket;
import com.cloudbees.booking.model.Receipt;

public interface BookingService {

    Ticket book(Receipt receipt);

    Receipt findReceipt(String emailAddress);

    Receipt cancelBooking(String emailAddress);
}
