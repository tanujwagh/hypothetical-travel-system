package com.example.travel.service.touch;

public interface EventProcessingService<T, Q> {
    Q process(T t);
}
