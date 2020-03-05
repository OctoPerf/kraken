package com.kraken.storage.client;

import com.kraken.storage.client.properties.StorageClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
class StorageClientConfiguration {

  @Bean("webClientStorage")
  WebClient storageWebClient(final StorageClientProperties properties) {
    return WebClient
        .builder()
        .baseUrl(properties.getStorageUrl())
        .build();
  }

}
