package com.ojt.klb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class BankingConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingConfigServiceApplication.class, args);
    }

}
