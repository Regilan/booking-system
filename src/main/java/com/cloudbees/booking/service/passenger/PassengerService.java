package com.cloudbees.booking.service.passenger;

import com.cloudbees.booking.model.Passenger;

public interface PassengerService {

    void createNewPassenger(Passenger passenger);

    Passenger getPassenger(String emailAddress);

}
