package com.kraken.commons.storage.synchronizer;

import com.kraken.commons.storage.client.StorageClient;
import com.kraken.commons.storage.synchronizer.properties.StorageSynchronizerProperties;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

  @Bean
  StorageSynchronizerProperties properties() {
    return StorageSynchronizerProperties.builder()
        .updateFilter("todo")
        .downloadFolder("gatling")
        .build();
  }


  @Bean
  StorageClient storageClient() {
    return Mockito.mock(StorageClient.class);
  }
}

