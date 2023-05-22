package com.example.travel.service.fair;

import com.example.travel.config.FairConfig;
import com.example.travel.event.TouchProcessedEvent;
import com.example.travel.event.TouchProcessedEventType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultFairProcessingService implements FairProcessingService<TouchProcessedEvent> {
    private final FairConfig fairConfig;
    private final Map<String, Map<String, Float>> routes;

    public DefaultFairProcessingService(FairConfig fairConfig) {
        this.fairConfig = fairConfig;
        routes = initializeRoutes();
    }

    private Map<String, Map<String, Float>> initializeRoutes() {
        Map<String, Map<String, Float>> routes = new HashMap<>();
        fairConfig.getRules().forEach(rule -> {
            //Forward Mapping
            Map<String, Float> destinationCosts = routes.getOrDefault(rule.getSource(), new HashMap<>());
            destinationCosts.put(rule.getDestination(), rule.getCost());
            routes.put(rule.getSource(), destinationCosts);

            //Reverse Mapping
            destinationCosts = routes.getOrDefault(rule.getDestination(), new HashMap<>());
            destinationCosts.put(rule.getSource(), rule.getCost());
            routes.put(rule.getDestination(), destinationCosts);
        });
        return routes;
    }

    @Override
    public void applyFair(TouchProcessedEvent event) {
        if(event.getType().equals(TouchProcessedEventType.COMPLETE)){
            event.setChargeAmount(completedTripChargeAmount(event.getSourceStopId(), event.getDestinationStopId()));
        } else if (event.getType().equals(TouchProcessedEventType.INCOMPLETE)) {
            event.setChargeAmount(incompleteTripChargeAmount(event.getSourceStopId()));
        }else {
            event.setChargeAmount(cancelledTripChargeAmount());
        }
    }

    private Float completedTripChargeAmount(String source, String destination) {
        return routes.get(source).get(destination);
    }

    private Float incompleteTripChargeAmount(String source) {
        Map<String, Float> destinationCosts = routes.get(source);
        Float maxCost = Float.MIN_VALUE;
        for (Map.Entry<String, Float> entry : destinationCosts.entrySet()) {
            maxCost = Math.max(maxCost, entry.getValue());
        }
        return maxCost;
    }

    private Float cancelledTripChargeAmount(){
        return 0f;
    }
}
