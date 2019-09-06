package com.kraken.storage.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageClientPropertiesTestConfiguration {

  @Bean
  StorageClientProperties storageProperties() {
    return StorageClientPropertiesTest.STORAGE_PROPERTIES;
  }

}
