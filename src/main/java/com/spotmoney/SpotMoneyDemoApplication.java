package com.spotmoney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpotMoneyDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpotMoneyDemoApplication.class, args);
	}
}
