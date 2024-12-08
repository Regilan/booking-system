package com.cloudbees.booking.e2e;

import com.cloudbees.booking.dto.BookingStatus;
import com.cloudbees.booking.dto.Ticket;
import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.model.Receipt;
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

    private final static String EMAIL_ADDRESS = "abc@example.com";

    @BeforeEach
    void setup() {
        passengerRepository.deleteAll();
        receiptRepository.deleteAll();
    }

    @Test
    void verifyReceiptIsReceivedAndTicketIsBooked() {
        createNewPassenger();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final String requestBody = "{" +
                "\"from\": \"London\"," +
                "\"to\": \"France\"," +
                "\"farePaid\": 20.0" +
                "}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Ticket> response = testRestTemplate.postForEntity("/book/%s".formatted(EMAIL_ADDRESS), request, Ticket.class);

        List<Receipt> receipts = receiptRepository.findAll();
        Assertions.assertEquals(1, receipts.size());

        Ticket ticket = assertResponse(response);
        Assertions.assertAll(
                () -> Assertions.assertEquals(EMAIL_ADDRESS, ticket.getPassenger().getEmailAddress()),
                () -> Assertions.assertNotNull(ticket.getReceipt().getId()),
                () -> Assertions.assertEquals(BookingStatus.CONFIRMED, ticket.getReceipt().getStatus()),
                () -> Assertions.assertNotNull(ticket.getSeatAllocated())
        );
    }

    @Test
    void shouldReturnReceiptForUser() {
        final Passenger passenger = createNewPassenger();
        addReceipt(passenger);

        ResponseEntity<Receipt> response = testRestTemplate.getForEntity("/receipt/%s".formatted(EMAIL_ADDRESS), Receipt.class);

        Receipt receipt = assertResponse(response);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(receipt.getId()),
                () -> Assertions.assertNotNull(receipt.getPassenger())
        );
    }

    @Test
    void verifyBookedReceiptIsCancelled() {
        final Passenger passenger = createNewPassenger();
        addReceipt(passenger);

        ResponseEntity<Receipt> response = testRestTemplate.postForEntity("/cancel-booking/%s".formatted(EMAIL_ADDRESS), null, Receipt.class);

        Receipt receipt = assertResponse(response);
        Assertions.assertEquals(BookingStatus.CANCELLED, receipt.getBookingStatus());
    }

    private Passenger createNewPassenger() {
        final Passenger passenger = new Passenger(EMAIL_ADDRESS, "Abc", "Xyz");
        return passengerRepository.save(passenger);
    }

    private void addReceipt(Passenger passenger) {
        final Receipt receiptToSave = new Receipt("London", "France", 20.0f).forPassenger(passenger);
        receiptToSave.setBookingStatus(BookingStatus.CONFIRMED);
        receiptRepository.save(receiptToSave);
    }

    private <T> T assertResponse(ResponseEntity<T> response) {
        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        return response.getBody();
    }

}
