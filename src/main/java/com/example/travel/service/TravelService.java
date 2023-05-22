package com.example.travel.service;

import com.example.travel.dto.CSVDataRow;
import com.example.travel.event.*;
import com.example.travel.service.fair.FairProcessingService;
import com.example.travel.service.reader.CSVReadingProcessingService;
import com.example.travel.service.summary.TripSummaryProcessingService;
import com.example.travel.service.touch.EventProcessingService;
import com.example.travel.service.touch.TouchEventProcessingService;
import com.example.travel.service.writer.FileWriterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@Profile("!test")
public class TravelService implements CommandLineRunner {

    private static final Pattern FILENAME_ARG_PATTERN = Pattern.compile("^--filename=(\\S*)");

    private final CSVReadingProcessingService csvReadingProcessingService;
    private final EventProcessingService<List<TouchEvent>, List<TouchProcessedEvent>> touchEventProcessingService;

    private final FileWriterService<List<TouchProcessedEvent>> tripReportCsvFileWriterService;
    private final FileWriterService<List<TripSummaryEvent>> tripSummaryCsvFileWriterService;
    private final TripSummaryProcessingService tripSummaryProcessingService;

    private final FairProcessingService<TouchProcessedEvent> defaultFairProcessingService;

    TravelService(CSVReadingProcessingService csvReadingProcessingService, TouchEventProcessingService touchEventProcessingService, FileWriterService<List<TouchProcessedEvent>> tripReportCsvFileWriterService, FileWriterService<List<TripSummaryEvent>> tripSummaryCsvFileWriterService, TripSummaryProcessingService tripSummaryProcessingService, FairProcessingService<TouchProcessedEvent> defaultFairProcessingService) {
        this.csvReadingProcessingService = csvReadingProcessingService;
        this.touchEventProcessingService = touchEventProcessingService;
        this.tripReportCsvFileWriterService = tripReportCsvFileWriterService;
        this.tripSummaryCsvFileWriterService = tripSummaryCsvFileWriterService;
        this.tripSummaryProcessingService = tripSummaryProcessingService;
        this.defaultFairProcessingService = defaultFairProcessingService;
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
        processedEvents.forEach(defaultFairProcessingService::applyFair);

        List<TouchProcessedEvent> tripsData = processedEvents.stream().filter(e -> e.getType().equals(TouchProcessedEventType.COMPLETE) || e.getType().equals(TouchProcessedEventType.CANCELLED) || e.getType().equals(TouchProcessedEventType.INCOMPLETE)).toList();
        tripReportCsvFileWriterService.write("trips", tripsData);

        List<TouchProcessedEvent> failedData = processedEvents.stream().filter(e -> e.getType().equals(TouchProcessedEventType.FAILED)).toList();
        tripReportCsvFileWriterService.write("unprocessableTouchData", failedData);

        List<TripSummaryEvent> tripSummaryEvents = tripSummaryProcessingService.processSummary(processedEvents);
        tripSummaryCsvFileWriterService.write("summary", tripSummaryEvents);
    }

    private String getFileName(String... args) {
        return Arrays.stream(args).map(arg -> {
                    var matcher = FILENAME_ARG_PATTERN.matcher(arg);
                    return matcher.matches() ? matcher.group(1) : null;
                }
        ).filter(Objects::nonNull).findFirst().orElseThrow(() -> new RuntimeException("Input file is missing"));
    }

}
