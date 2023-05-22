package com.example.travel.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "fair")
public class FairConfig {
   private String currency;
   private List<FairRule> rules;

   @Getter
   @Setter
   public static class FairRule {
      private String source;
      private String destination;
      private Float cost;
   }
}
