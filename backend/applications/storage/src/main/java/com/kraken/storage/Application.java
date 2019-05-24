package com.kraken.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kraken.storage", "com.kraken.commons"})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}