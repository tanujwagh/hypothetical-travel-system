package com.example.travel.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TouchEvent {
    @NotNull(message = "Id is missing")
    private Long id;

    @NotNull(message = "Data Time is missing")
    private ZonedDateTime dateTime;

    @NotNull(message = "Touch type is missing")
    private TouchEventType touchType;

    @NotEmpty(message = "Stop Id is missing")
    private String stopId;

    @NotEmpty(message = "Company Id is missing")
    private String companyId;

    @NotEmpty(message = "Bus Id is missing")
    private String busId;

    @NotEmpty(message = "PAN is missing")
    private String pan;
}
