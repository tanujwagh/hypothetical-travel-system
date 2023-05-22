package com.example.travel.service;

import com.example.travel.service.fair.DefaultFairProcessingService;
import com.example.travel.event.TouchProcessedEvent;
import com.example.travel.event.TouchProcessedEventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class DefaultFairProcessingServiceTest {

    @Autowired
    DefaultFairProcessingService defaultFairProcessingService;

    @Test
    void testCompleteTrip_caseSuccess() {
        var source = "StopA";
        var destination = "StopB";
        var expectedCost = 4.50f;

        var event = new TouchProcessedEvent(
                ZonedDateTime.now(),
                ZonedDateTime.now().plusMinutes(10),
                600L,
                source,
                destination,
                0.0f,
                "Company1",
                "Bus1",
                "123456789",
                TouchProcessedEventType.COMPLETE,
                "COMPLETE"
        );


        defaultFairProcessingService.applyFair(event);

        assertThat(event)
                .extracting(TouchProcessedEvent::getChargeAmount)
                .isNotNull()
                .isEqualTo(expectedCost);
    }

    @Test
    void testIncompleteTrip_caseSuccess(){
        var source = "StopA";
        var expectedCost = 8.45f;

        var event = new TouchProcessedEvent(
                ZonedDateTime.now(),
                ZonedDateTime.now().plusMinutes(10),
                600L,
                source,
                source,
                0.0f,
                "Company1",
                "Bus1",
                "123456789",
                TouchProcessedEventType.INCOMPLETE,
                "INCOMPLETE"
        );


        defaultFairProcessingService.applyFair(event);

        assertThat(event)
                .extracting(TouchProcessedEvent::getChargeAmount)
                .isNotNull()
                .isEqualTo(expectedCost);
    }

    @Test
    void testCancelledTrip_caseSuccess(){
        var source = "StopA";
        var destination = "StopA";
        var expectedCost = 0f;

        var event = new TouchProcessedEvent(
                ZonedDateTime.now(),
                ZonedDateTime.now().plusMinutes(10),
                600L,
                source,
                destination,
                0.0f,
                "Company1",
                "Bus1",
                "123456789",
                TouchProcessedEventType.CANCELLED,
                "CANCELLED"
        );


        defaultFairProcessingService.applyFair(event);

        assertThat(event)
                .extracting(TouchProcessedEvent::getChargeAmount)
                .isNotNull()
                .isEqualTo(expectedCost);
    }

}
