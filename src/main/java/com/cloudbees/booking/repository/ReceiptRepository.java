package com.cloudbees.booking.repository;

import com.cloudbees.booking.model.Receipt;
import jakarta.persistence.EntityExistsException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Optional<Receipt> findByPassenger_EmailAddress(final String emailAddress);

    default Receipt saveIfAbsent(final Receipt receipt) {
        Optional<Receipt> existingReceipt = findByPassenger_EmailAddress(receipt.getPassenger().getEmailAddress());
        if (existingReceipt.isEmpty()) {
            return save(receipt);
        }
        throw new EntityExistsException();
    }

}
