package com.example.travel.service;

import com.example.travel.touch.domain.TouchProcessedEvent;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CSVFileWriterService implements FileWriterService<List<TouchProcessedEvent>> {
    private static final List<String> HEADER = List.of(
            "started",
            "finished",
            "durationSec",
            "fromStopId",
            "toStopId",
            "chargeAmount",
            "companyId",
            "busId",
            "hashedPan",
            "status"
    );

    private static final String FILE_EXTENSION = "csv";

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public void write(String filename, List<TouchProcessedEvent> events) throws FileNotFoundException {
        File csvOutputFile = new File(String.format("%s.%s", filename, FILE_EXTENSION));
        try (PrintWriter writer = new PrintWriter(csvOutputFile)) {
            writer.println(HEADER.stream().collect(Collectors.joining(",")));
            events.stream()
                    .map(event -> getCSVRow(event))
                    .forEach(writer::println);
        }
    }

    private String getCSVRow(TouchProcessedEvent event) {
        return String.join(",",
                event.getStartDateTime().format(dateTimeFormatter),
                event.getEndDateTime().format(dateTimeFormatter),
                event.getDurationSec().toString(),
                event.getSourceStopId(),
                event.getDestinationStopId(),
                event.getChargeAmount().toString(),
                event.getCompanyId(),
                event.getBusId(),
                event.getPan(),
                event.getStatus()
        );
    }
}
