package com.example.travel.service.writer;

import com.example.travel.event.TripSummaryEvent;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripSummaryCSVFileWriterService implements FileWriterService<List<TripSummaryEvent>> {
    private static final List<String> HEADER = List.of(
            "date",
            "companyId",
            "busId",
            "completeTripCount",
            "incompleteTripCount",
            "cancelledTripCount",
            "totalCharges"
    );

    private static final String FILE_EXTENSION = "csv";

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public File write(String filename, List<TripSummaryEvent> events) throws FileNotFoundException {
        File csvOutputFile = new File(String.format("%s.%s", filename, FILE_EXTENSION));
        try (PrintWriter writer = new PrintWriter(csvOutputFile)) {
            writer.println(HEADER.stream().collect(Collectors.joining(",")));
            events.stream()
                    .map(event -> getCSVRow(event))
                    .forEach(writer::println);
        }
        return csvOutputFile;
    }

    private String getCSVRow(TripSummaryEvent event) {
        return String.join(",",
                event.getDate().format(dateTimeFormatter),
                event.getCompanyId(),
                event.getBusId(),
                event.getCompleteTripCount().toString(),
                event.getIncompleteTripCount().toString(),
                event.getCancelledTripCount().toString(),
                String.format("%.2f", event.getTotalCharges())
        );
    }
}
