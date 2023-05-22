package com.example.travel.core.handler;

import com.example.travel.core.domain.TravelEvent;

public interface EventHandler<T extends TravelEvent>{
    void handleEvent(T event);
}
