package com.cloudbees.booking.e2e;

import com.cloudbees.booking.model.Passenger;
import com.cloudbees.booking.repository.PassengerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PassengerControllerITest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PassengerRepository passengerRepository;

    @BeforeEach
    void setup() {
        passengerRepository.deleteAll();
    }

    @Test
    void verifyNewPassengerIsCreated() {
        final Passenger passenger = new Passenger("abc@example.com", "Abc", "Xyz");
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/user", passenger, Void.class);

        Assertions.assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());

        List<Passenger> passengers = passengerRepository.findAll();
        long result = passengers.stream()
                .filter(value -> "abc@example.com".equals(value.getEmailAddress()))
                .count();
        Assertions.assertEquals(1, result);
    }

}
