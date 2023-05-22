package com.example.travel.core.handler;

import com.example.travel.core.domain.TravelEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
public final class ApplicationEventHandler {
    private final Map<Class<? extends TravelEvent>, EventHandler> handlers = new HashMap<>();

    public <T extends TravelEvent> void registerHandler(Class<T> clazz, EventHandler<T> handler) {
        handlers.put(clazz, handler);
    }

    public <T extends TravelEvent> void publishEvent(T event) {
        Optional.ofNullable(handlers.get(event.getClass()))
                .ifPresentOrElse(
                        handler -> handler.handleEvent(event),
                        () -> {
                            throw new IllegalArgumentException();
                        }
                );
    }
}
