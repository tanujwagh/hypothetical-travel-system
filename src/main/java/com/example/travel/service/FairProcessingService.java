package com.example.travel.service;

import com.example.travel.config.FairConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FairProcessingService {
    private final FairConfig fairConfig;
    private final Map<String, Map<String, Float>> routes;

    public FairProcessingService(FairConfig fairConfig) {
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

    public Float processCompletedTripFair(String source, String destination) {
        return routes.get(source).get(destination);
    }

    public Float processIncompleteTripFair(String source) {
        Map<String, Float> destinationCosts = routes.get(source);
        Float maxCost = Float.MIN_VALUE;
        for (Map.Entry<String, Float> entry : destinationCosts.entrySet()) {
            maxCost = Math.max(maxCost, entry.getValue());
        }
        return maxCost;
    }

    public Float processCancelledTripFair(String source, String destination){
        return 0f;
    }

}
