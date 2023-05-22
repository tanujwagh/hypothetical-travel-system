package com.example.travel.service;

import com.example.travel.dto.CSVDataRow;
import com.example.travel.touch.domain.TouchEvent;
import com.example.travel.touch.domain.TouchEventType;
import com.example.travel.touch.domain.TouchProcessedEvent;
import com.example.travel.touch.domain.TouchProcessedEventType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class TravelService implements CommandLineRunner{

    private static final Pattern FILENAME_ARG_PATTERN = Pattern.compile("^--filename=(\\S*)");

    private final CSVReadingProcessingService csvReadingProcessingService;
    private final EventProcessingService<List<TouchEvent>, List<TouchProcessedEvent>> touchEventProcessingService;

    private final FileWriterService<List<TouchProcessedEvent>> csvFileWriterService;
    private final TripSummaryProcessingService tripSummaryProcessingService;

    TravelService(CSVReadingProcessingService csvReadingProcessingService, TouchEventProcessingService touchEventProcessingService, FileWriterService<List<TouchProcessedEvent>> csvFileWriterService, TripSummaryProcessingService tripSummaryProcessingService) {
        this.csvReadingProcessingService = csvReadingProcessingService;
        this.touchEventProcessingService = touchEventProcessingService;
        this.csvFileWriterService = csvFileWriterService;
        this.tripSummaryProcessingService = tripSummaryProcessingService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> rows = csvReadingProcessingService.read(getFileName(args));
        List<CSVDataRow> csvRows = csvReadingProcessingService.process(rows);
        List<TouchEvent> touchEvents = csvRows.stream().map(row -> new TouchEvent(
                Long.parseLong(row.getId()),
                row.getDateTime(),
                TouchEventType.valueOf(row.getTouchType()),
                row.getStopId(),
                row.getCompanyId(),
                row.getBusId(),
                row.getPan()
        )).toList();
        List<TouchProcessedEvent> processedEvents = touchEventProcessingService.process(touchEvents);

        List<TouchProcessedEvent> tripsData = processedEvents.stream().filter(e -> e.getType().equals(TouchProcessedEventType.COMPLETE) || e.getType().equals(TouchProcessedEventType.CANCELLED)).toList();
        csvFileWriterService.write("trips", tripsData);

        List<TouchProcessedEvent> failedData = processedEvents.stream().filter(e -> e.getType().equals(TouchProcessedEventType.FAILED)).toList();
        csvFileWriterService.write("unprocessableTouchData", failedData);

        tripSummaryProcessingService.getSummary(processedEvents);
    }

    private String getFileName(String... args){
        return Arrays.stream(args).map(arg -> {
                    var matcher = FILENAME_ARG_PATTERN.matcher(arg);
                    return matcher.matches() ? matcher.group(1) : null;
                }
                ).filter(Objects::nonNull).findFirst().orElseThrow(() -> new RuntimeException("Input file is missing"));
    }

}
