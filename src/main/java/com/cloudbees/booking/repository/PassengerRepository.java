package com.cloudbees.booking.repository;

import com.cloudbees.booking.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, String> {
}
