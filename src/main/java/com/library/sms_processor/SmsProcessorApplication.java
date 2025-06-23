package com.library.sms_processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmsProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsProcessorApplication.class, args);
	}

}
