package com.example.travel.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "input-file")
public class InputFileConfig {
    private String type;
    private String delimiter;
    private String dateTimeFormat;
    private List<String> headers;
}
