package com.kraken.storage.client.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageClientPropertiesTestConfiguration {

  @Bean
  StorageClientProperties storageProperties() {
    return StorageClientPropertiesTest.STORAGE_PROPERTIES;
  }

}
