package com.kraken.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kraken.executor", "com.kraken.commons"})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}