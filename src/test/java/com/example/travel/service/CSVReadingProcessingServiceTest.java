package com.example.travel.service;

import com.example.travel.dto.CSVDataRow;
import com.example.travel.service.reader.CSVReadingProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class CSVReadingProcessingServiceTest {

    @Autowired
    CSVReadingProcessingService csvReadingProcessingService;

    @Test
    void testProcess_caseSuccess() {
        String line = "1,16-05-2023 12:15:00, ON, StopA, Company1, Bus10, 2255550000666662";

        List<CSVDataRow> csvRows = csvReadingProcessingService.process(List.of(line));

        assertThat(csvRows)
                .hasSize(1)
                .element(0)
                .extracting(CSVDataRow::getPan)
                .isEqualTo("2255550000666662");

    }
}
