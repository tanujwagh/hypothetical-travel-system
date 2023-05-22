package com.example.travel.service.summary;

import com.example.travel.event.TouchProcessedEvent;
import com.example.travel.event.TouchProcessedEventType;
import com.example.travel.event.TripSummaryEvent;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class TripSummaryProcessingService implements SummaryProcessingService<List<TouchProcessedEvent>, List<TripSummaryEvent>> {


    @Override
    public List<TripSummaryEvent> processSummary(List<TouchProcessedEvent> events){
        Map<ZonedDateTime, Map<String, Map<String, List<TouchProcessedEvent>>>> summaryData = events.stream()
                .map(event -> {
                    event.setStartDateTime(event.getStartDateTime().withHour(0).withMinute(0).withSecond(0));
                    event.setEndDateTime(event.getEndDateTime().withHour(0).withMinute(0).withSecond(0));
                    return event;
                }).collect(groupingBy(TouchProcessedEvent::getStartDateTime, groupingBy(TouchProcessedEvent::getCompanyId, groupingBy(TouchProcessedEvent::getBusId))));

        List<TripSummaryEvent> tripSummaryEvents = new ArrayList();
        summaryData.entrySet().forEach(timeEntry -> {
            ZonedDateTime date = timeEntry.getKey();
            timeEntry.getValue().entrySet().forEach(company -> {
                String companyId = company.getKey();
                company.getValue().entrySet().forEach(bus -> {
                    String busId = bus.getKey();
                    Double totalAmount = 0.0;
                    long completeCount = 0;
                    long incompleteCount = 0;
                    long cancelledCount = 0;
                    for(TouchProcessedEvent event : bus.getValue()) {
                        totalAmount += event.getChargeAmount();
                        completeCount += event.getType().equals(TouchProcessedEventType.COMPLETE) ? 1 : 0;
                        incompleteCount += event.getType().equals(TouchProcessedEventType.INCOMPLETE) ? 1 : 0;
                        cancelledCount += event.getType().equals(TouchProcessedEventType.CANCELLED) ? 1 : 0;
                    }
                    TripSummaryEvent tripSummaryEvent = new TripSummaryEvent(
                            date,
                            companyId,
                            busId,
                            completeCount,
                            incompleteCount,
                            cancelledCount,
                            totalAmount
                    );
                    tripSummaryEvents.add(tripSummaryEvent);
                });
            });
        });
        return tripSummaryEvents;
    }

}
