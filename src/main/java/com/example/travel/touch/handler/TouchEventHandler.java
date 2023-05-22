package com.example.travel.touch.handler;

import com.example.travel.core.handler.EventHandler;
import com.example.travel.touch.domain.TouchEvent;
import com.example.travel.touch.domain.TouchEventType;
import com.example.travel.touch.domain.TouchProcessedEvent;
import com.example.travel.touch.domain.TouchProcessedEventType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class TouchEventHandler implements EventHandler<TouchEvent> {

    @Override
    public void handleEvent(TouchEvent event) {

    }



    private boolean checkIfValidTouch(TouchEvent touchEvent, TouchEvent newEvent) {
        return touchEvent.getTouchType().equals(TouchEventType.ON) &&
                newEvent.getTouchType().equals(TouchEventType.OFF) &&
                touchEvent.getBusId().equals(touchEvent.getBusId());
    }
}
