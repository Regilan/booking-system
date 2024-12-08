package com.cloudbees.booking.repository;

import com.cloudbees.booking.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
