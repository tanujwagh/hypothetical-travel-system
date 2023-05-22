package com.example.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CSVDataRow {
    private String id;
    private ZonedDateTime dateTime;
    private String touchType;
    private String StopId;
    private String companyId;
    private String busId;
    private String pan;
}
