package com.example.travel.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TripSummaryEvent {
    ZonedDateTime date;
    String companyId;
    String busId;
    Long completeTripCount;
    Long incompleteTripCount;
    Long cancelledTripCount;
    Double totalCharges;
}
