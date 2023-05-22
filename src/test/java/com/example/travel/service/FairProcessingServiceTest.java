package com.example.travel.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FairProcessingServiceTest {

    @Autowired
    FairProcessingService fairProcessingService;

    @Test
    void testProcessCompletedTripFair_caseSuccess() {
        var source = "StopA";
        var destination = "StopB";
        var expectedCost = 4.50f;

        var fairAToB = fairProcessingService.processCompletedTripFair(source, destination);
        var fairBToA = fairProcessingService.processCompletedTripFair(destination, source);

        assertThat(fairAToB).isEqualTo(fairBToA).isEqualTo(expectedCost);
    }

    @Test
    void testProcessIncompleteTripFair_caseSuccess(){

    }

}
