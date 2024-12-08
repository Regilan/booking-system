package com.cloudbees.booking.e2e;

import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.model.Receipt;
import com.cloudbees.booking.model.Ticket;
import com.cloudbees.booking.repository.PassengerRepository;
import com.cloudbees.booking.repository.ReceiptRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingControllerITest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @BeforeEach
    void setup() {
        passengerRepository.deleteAll();
        receiptRepository.deleteAll();
    }

    @Test
    void verifyReceiptIsReceivedAndTicketIsBooked() {
        final String emailId = "abc@example.com";
        final Passenger passenger = new Passenger(emailId, "Abc", "Xyz");
        passengerRepository.save(passenger);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final String requestBody = "{" +
                "\"from\": \"London\"," +
                "\"to\": \"France\"," +
                "\"emailAddress\": \"%s\",".formatted(emailId) +
                "\"farePaid\": 20.0" +
                "}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Ticket> response = testRestTemplate.postForEntity("/book", request, Ticket.class);

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        List<Receipt> receipts = receiptRepository.findAll();
        Assertions.assertEquals(1, receipts.size());

        Ticket ticket = response.getBody();
        Assertions.assertAll(
                () -> Assertions.assertEquals(emailId, ticket.getPassenger().getEmailAddress()),
                () -> Assertions.assertNotNull(ticket.getReceipt().getId()),
                () -> Assertions.assertNotNull(ticket.getSeatAllocated()),
                () -> Assertions.assertNull(ticket.getReceipt().getEmailAddress())
        );
    }

}
