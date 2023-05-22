package com.example.travel.config;

import com.example.travel.touch.domain.TouchEvent;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfig {

    @Bean
    public Validator validator(){
        return Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Bean("touchEventStore")
    public Map<String, TouchEvent> touchEventStore(){
        return new HashMap<>();
    }
}
