package com.ctv_it.klb;

import com.ctv_it.klb.util.mock.MockDataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BankingReportServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankingReportServiceApplication.class, args);
    MockDataService.initInstance().getAll();
  }
}
