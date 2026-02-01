package com.smartbiz.smartbiz_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SmartbizApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartbizApiApplication.class, args);
	}

}
