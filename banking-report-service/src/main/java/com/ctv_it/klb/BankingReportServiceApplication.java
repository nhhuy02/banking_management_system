package com.ctv_it.klb;

import com.ctv_it.klb.util.FakeMockServices.FakeDataGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingReportServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankingReportServiceApplication.class, args);
    FakeDataGenerator.initInstance().getAll();
  }
}
