package com.example.travel.service;

import com.example.travel.service.writer.TripReportCSVFileWriterService;
import com.example.travel.event.TouchProcessedEvent;
import com.example.travel.event.TouchProcessedEventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TripReportCSVFileWriterServiceTest {

    @Autowired
    TripReportCSVFileWriterService tripReportCsvFileWriterService;

    @Test
    void testWrite_caseSuccess() throws FileNotFoundException {
        var event = new TouchProcessedEvent(
                ZonedDateTime.now(),
                ZonedDateTime.now().plusMinutes(10),
                600L,
                "StopA",
                "StopB",
                4.50f,
                "Company1",
                "Bus1",
                "123456789",
                TouchProcessedEventType.COMPLETE,
                "COMPLETE"
        );
        var file = tripReportCsvFileWriterService.write("test", List.of(event));

        assertThat(file)
                .isNotNull()
                .isFile()
                .hasFileName("test.csv")
                .hasExtension("csv");
    }

}
