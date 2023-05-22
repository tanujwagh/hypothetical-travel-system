package com.example.travel.service.summary;

public interface SummaryProcessingService<T, Q> {
    Q processSummary(T t);
}
