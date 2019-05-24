package com.kraken.commons.storage.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kraken.commons.storage.service"})
class StorageServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(StorageServiceApplication.class, args);
  }
}