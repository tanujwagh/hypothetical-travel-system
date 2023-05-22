package com.example.travel.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TouchProcessedEvent {
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private Long durationSec;
    private String sourceStopId;
    private String destinationStopId;
    private Float chargeAmount;
    private String companyId;
    private String busId;
    private String pan;
    private TouchProcessedEventType type;
    private String status;
}
