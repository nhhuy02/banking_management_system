package com.ojt.klb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BankingSimulatorApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingSimulatorApiServiceApplication.class, args);
	}

}
