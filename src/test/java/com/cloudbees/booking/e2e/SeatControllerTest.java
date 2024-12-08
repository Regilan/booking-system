package com.cloudbees.booking.e2e;

import com.cloudbees.booking.dto.BookingStatus;
import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.model.Receipt;
import com.cloudbees.booking.model.Seat;
import com.cloudbees.booking.repository.PassengerRepository;
import com.cloudbees.booking.repository.ReceiptRepository;
import com.cloudbees.booking.repository.SeatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeatControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private SeatRepository seatRepository;

    @BeforeEach
    void setUp() {
        cleanDb();
        Passenger passenger1 = new Passenger("abc@gmail.com", "Abc", "Def");
        Passenger passenger2 = new Passenger("mno@gmail.com", "Mno", "Pqr");
        passengerRepository.saveAll(List.of(passenger1, passenger2));

        Receipt receipt1 = new Receipt("London", "Paris", 20.0f);
        receipt1.forPassenger(passenger1);
        Receipt receipt2 = new Receipt("London", "Paris", 20.0f);
        receipt2.forPassenger(passenger2);

        Seat seat1 = new Seat("A1");
        Seat seat2 = new Seat("A2");
        Seat seat3 = new Seat("B1");
        Seat seat4 = new Seat("B2");
        seatRepository.saveAll(List.of(seat1, seat2, seat3, seat4));

        receipt1.assignSeat(seat1);
        receipt2.assignSeat(seat2);
        receiptRepository.saveIfAbsent(receipt1);
        receiptRepository.saveIfAbsent(receipt2);
        seatRepository.saveAll(List.of(seat1, seat2));
    }

    @AfterEach
    void cleanUp() {
        cleanDb();
    }

    private void cleanDb() {
        receiptRepository.deleteAll();
        seatRepository.deleteAll();
        passengerRepository.deleteAll();
    }

    @Test
    void shouldListSeats() {
        ResponseEntity<Map<String, List<Seat>>> response = fetchSeatsFromAPI("/seat");

        Map<String, List<Seat>> seats = response.getBody();
        Assertions.assertNotNull(seats);
        assertSeatSectionSize(seats, 2);

        assertSectionSeats(seats, "A", false);
        assertSectionSeats(seats, "B", true);
    }

    @Test
    void shouldListSeatsFromRequestedSection() {
        ResponseEntity<Map<String, List<Seat>>> response = fetchSeatsFromAPI("/seat?section=B");

        Map<String, List<Seat>> seats = response.getBody();
        Assertions.assertNotNull(seats);
        assertSeatSectionSize(seats, 1);

        assertSectionSeats(seats, "B", true);
    }

    private ResponseEntity<Map<String, List<Seat>>> fetchSeatsFromAPI(String url) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, List<Seat>>>() {
                }
        );
    }

    private void assertSeatSectionSize(Map<String, List<Seat>> seats, int expectedSize) {
        Assertions.assertEquals(expectedSize, seats.keySet().size());
    }

    private void assertSectionSeats(Map<String, List<Seat>> seats, String section, boolean expectedAvailability) {
        List<Seat> sectionSeats = seats.get(section);
        Assertions.assertEquals(2, sectionSeats.size());

        for (Seat seat : sectionSeats) {
            Assertions.assertEquals(expectedAvailability, seat.getIsAvailable());
            if (expectedAvailability) {
                Assertions.assertNull(seat.getPassenger());
            } else {
                Assertions.assertNotNull(seat.getPassenger());
            }
        }
    }

}
