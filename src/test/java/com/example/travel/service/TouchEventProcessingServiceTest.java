package com.example.travel.service;

import com.example.travel.service.touch.TouchEventProcessingService;
import com.example.travel.event.TouchEvent;
import com.example.travel.event.TouchEventType;
import com.example.travel.event.TouchProcessedEvent;
import com.example.travel.event.TouchProcessedEventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TouchEventProcessingServiceTest {

    @Autowired
    TouchEventProcessingService touchEventProcessingService;


    @Test
    void testProcess_completeTrip_caseSuccess() {
        TouchEvent e1 = new TouchEvent(
                1L,
                ZonedDateTime.now(),
                TouchEventType.ON,
                "StopA",
                "Company1",
                "Bus1",
                "123456"
        );

        TouchEvent e2 = new TouchEvent(
                1L,
                ZonedDateTime.now().plusHours(1),
                TouchEventType.OFF,
                "StopB",
                "Company1",
                "Bus1",
                "123456"
        );


        List<TouchProcessedEvent> processedEvents = touchEventProcessingService.process(List.of(e1, e2));

        assertThat(processedEvents)
                .hasSize(1)
                .element(0)
                .extracting(TouchProcessedEvent::getType).isEqualTo(TouchProcessedEventType.COMPLETE);
    }

    @Test
    void testProcess_cancelledTrip_caseSuccess() {
        TouchEvent e1 = new TouchEvent(
                1L,
                ZonedDateTime.now(),
                TouchEventType.ON,
                "StopA",
                "Company1",
                "Bus1",
                "123456"
        );

        TouchEvent e2 = new TouchEvent(
                1L,
                ZonedDateTime.now().plusHours(1),
                TouchEventType.OFF,
                "StopA",
                "Company1",
                "Bus1",
                "123456"
        );


        List<TouchProcessedEvent> processedEvents = touchEventProcessingService.process(List.of(e1, e2));

        assertThat(processedEvents)
                .hasSize(1)
                .element(0)
                .extracting(TouchProcessedEvent::getType).isEqualTo(TouchProcessedEventType.CANCELLED);
    }

    @Test
    void testProcess_inCompleteTrip_caseSuccess() {
        TouchEvent e1 = new TouchEvent(
                1L,
                ZonedDateTime.now(),
                TouchEventType.ON,
                "StopA",
                "Company1",
                "Bus1",
                "123456"
        );


        List<TouchProcessedEvent> processedEvents = touchEventProcessingService.process(List.of(e1));

        assertThat(processedEvents)
                .hasSize(1)
                .element(0)
                .extracting(TouchProcessedEvent::getType).isEqualTo(TouchProcessedEventType.INCOMPLETE);
    }

    @Test
    void testProcess_failedTrip_caseSuccess() {
        TouchEvent e1 = new TouchEvent(
                1L,
                ZonedDateTime.now(),
                TouchEventType.ON,
                "StopA",
                "Company1",
                "Bus1",
                null
        );


        List<TouchProcessedEvent> processedEvents = touchEventProcessingService.process(List.of(e1));

        assertThat(processedEvents)
                .hasSize(1)
                .element(0)
                .extracting(TouchProcessedEvent::getType).isEqualTo(TouchProcessedEventType.FAILED);

        var result = processedEvents.get(0);
        assertThat(result.getStatus()).isEqualTo("PAN is missing");
    }

}
