package com.cloudbees.booking.service;

import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.repository.PassengerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    @Override
    public void createNewPassenger(Passenger passenger) {
        passengerRepository.save(passenger);
    }

    @Override
    public Passenger getPassenger(String emailAddress) {
        Optional<Passenger> passenger = passengerRepository.findById(emailAddress);
        return passenger.orElseThrow(EntityNotFoundException::new);
    }
}
