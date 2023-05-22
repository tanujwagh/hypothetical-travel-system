package com.example.travel.service;

import com.example.travel.touch.domain.TouchEvent;
import com.example.travel.touch.domain.TouchEventType;
import com.example.travel.touch.domain.TouchProcessedEvent;
import com.example.travel.touch.domain.TouchProcessedEventType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
public class TouchEventProcessingService implements EventProcessingService<List<TouchEvent>, List<TouchProcessedEvent>> {

    private final Validator validator;
    private final Map<String, TouchEvent> touchEventStore;


    public TouchEventProcessingService(Validator validator, @Qualifier("touchEventStore") Map<String, TouchEvent> touchEventStore) {
        this.validator = validator;
        this.touchEventStore = touchEventStore;
    }


    @Override
    public List<TouchProcessedEvent> process(List<TouchEvent> touchEvents) {
        return touchEvents.stream().map(event ->
                getValidationErrors(event)
                        .map( error -> new TouchProcessedEvent(
                                event.getDateTime(),
                                event.getDateTime(),
                                0L,
                                event.getStopId(),
                                event.getStopId(),
                                0.0f,
                                event.getCompanyId(),
                                event.getBusId(),
                                event.getPan(),
                                TouchProcessedEventType.FAILED,
                                error
                        ))
                        .orElseGet(() -> processEvent(event))
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private TouchProcessedEvent processEvent(TouchEvent newEvent) {
        //An event is valid if and only if there is ON entry and new entry is OFF
        if(newEvent.getTouchType().equals(TouchEventType.OFF)){
            if(touchEventStore.containsKey(newEvent.getPan())){
                TouchEvent oldEvent = touchEventStore.get(newEvent.getPan());
                TouchProcessedEvent touchProcessedEvent = new TouchProcessedEvent(
                        oldEvent.getDateTime(),
                        newEvent.getDateTime(),
                        ChronoUnit.SECONDS.between(oldEvent.getDateTime(), newEvent.getDateTime()),
                        oldEvent.getStopId(),
                        newEvent.getStopId(),
                        0.0f,
                        newEvent.getCompanyId(),
                        newEvent.getBusId(),
                        newEvent.getPan(),
                        null,
                        null
                );
                if(checkIfCompleted(oldEvent, newEvent)){
                    touchEventStore.remove(newEvent.getPan());
                    touchProcessedEvent.setType(TouchProcessedEventType.COMPLETE);
                    touchProcessedEvent.setStatus("COMPLETE");
                } else if (checkIfCancelled(oldEvent, newEvent)) {
                    touchEventStore.remove(newEvent.getPan());
                    touchProcessedEvent.setType(TouchProcessedEventType.CANCELLED);
                    touchProcessedEvent.setStatus("CANCELLED");
                } else{
                    touchProcessedEvent.setType(TouchProcessedEventType.FAILED);
                    touchProcessedEvent.setStatus("Data mismatch");
                }
                return touchProcessedEvent;
            }else{
                //All entries that do not have a valid ON event are considered failed
                return new TouchProcessedEvent(
                        newEvent.getDateTime(),
                        newEvent.getDateTime(),
                        0L,
                        newEvent.getStopId(),
                        newEvent.getStopId(),
                        0.0f,
                        newEvent.getCompanyId(),
                        newEvent.getBusId(),
                        newEvent.getPan(),
                        TouchProcessedEventType.FAILED,
                        "Touch Type ON entry missing"
                );
            }
        }else{
            touchEventStore.put(newEvent.getPan(), newEvent);
        }
        return null;
    }

    private Optional<String> getValidationErrors(TouchEvent touchEvent) {
        Set<ConstraintViolation<TouchEvent>> constraintViolations = validator.validate(touchEvent);
        return Optional.of(constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" ")))
                .filter(Predicate.not(StringUtils::isEmpty));
    }

    private boolean checkIfCompleted(TouchEvent oldEvent, TouchEvent newEvent) {
        return baseChecks(oldEvent, newEvent) && !oldEvent.getStopId().equals(newEvent.getStopId());
    }

    private boolean checkIfCancelled(TouchEvent oldEvent, TouchEvent newEvent) {
        return baseChecks(oldEvent, newEvent) && oldEvent.getStopId().equals(newEvent.getStopId());
    }

    private boolean baseChecks(TouchEvent oldEvent, TouchEvent newEvent) {
        return oldEvent.getTouchType().equals(TouchEventType.ON) &&
                newEvent.getTouchType().equals(TouchEventType.OFF) &&
                oldEvent.getBusId().equals(oldEvent.getBusId()) &&
                oldEvent.getDateTime().isBefore(newEvent.getDateTime());
    }
}
