package com.example.travel.service;

import com.example.travel.service.writer.TripSummaryCSVFileWriterService;
import com.example.travel.event.TripSummaryEvent;
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
public class TripSummaryCSVFileWriterServiceTest {

    @Autowired
    TripSummaryCSVFileWriterService tripSummaryCSVFileWriterService;

    @Test
    void testWrite_caseSuccess() throws FileNotFoundException {
        var event = new TripSummaryEvent(
                ZonedDateTime.now(),
                "Company1",
                "Bus1",
                1L,
                1L,
                1L,
                10.0
        );
        var file = tripSummaryCSVFileWriterService.write("test", List.of(event));

        assertThat(file)
                .isNotNull()
                .isFile()
                .hasFileName("test.csv")
                .hasExtension("csv");
    }

}
