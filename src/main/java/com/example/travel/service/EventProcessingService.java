package com.example.travel.service;

import com.example.travel.core.domain.TravelEvent;

public interface EventProcessingService<T, Q> {
    Q process(T t);
}
