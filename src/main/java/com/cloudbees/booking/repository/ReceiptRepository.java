package com.cloudbees.booking.repository;

import com.cloudbees.booking.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Optional<Receipt> findByPassenger_EmailAddress(final String emailAddress);

}
