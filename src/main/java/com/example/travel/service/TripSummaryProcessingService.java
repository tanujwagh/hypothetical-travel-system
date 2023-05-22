package com.example.travel.service;

import com.example.travel.touch.domain.TouchProcessedEvent;
import com.example.travel.touch.domain.TripSummaryEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class TripSummaryProcessingService {


    public void getSummary(List<TouchProcessedEvent> events){
        var data = events.stream()
                .map(event -> {
                    event.setStartDateTime(event.getStartDateTime().withHour(0).withMinute(0).withSecond(0));
                    event.setEndDateTime(event.getEndDateTime().withHour(0).withMinute(0).withSecond(0));
                    return event;
                }).collect(groupingBy(TouchProcessedEvent::getStartDateTime, groupingBy(TouchProcessedEvent::getCompanyId, groupingBy(TouchProcessedEvent::getBusId))));
        System.out.println(data.size());
    }

}
