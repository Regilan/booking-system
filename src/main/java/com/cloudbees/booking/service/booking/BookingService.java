package com.cloudbees.booking.service.booking;

import com.cloudbees.booking.model.Receipt;
import com.cloudbees.booking.model.Ticket;

public interface BookingService {

    Ticket book(Receipt receipt);

}
