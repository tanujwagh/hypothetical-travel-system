package com.example.travel;

import com.example.travel.config.ApplicationConfig;
import com.example.travel.config.FairConfig;
import com.example.travel.config.InputFileConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TravelApplicationTests {

	@Autowired
	ApplicationConfig applicationConfig;

	@Autowired
	FairConfig fairConfig;

	@Autowired
	InputFileConfig inputFileConfig;

	@Test
	void contextLoads() {
		assertThat(applicationConfig).isNotNull();
		assertThat(fairConfig).isNotNull();
		assertThat(inputFileConfig).isNotNull();
	}

}
