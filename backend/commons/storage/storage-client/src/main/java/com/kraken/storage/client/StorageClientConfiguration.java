package com.kraken.storage.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
class StorageClientConfiguration {

  @Autowired
  @Bean
  StorageClientProperties storageClientProperties(@Value("${kraken.storage.url:#{environment.KRAKEN_STORAGE_URL}}") final String storageUrl) {
    log.info("Storage URL is set to " + storageUrl);

    return StorageClientProperties.builder()
        .storageUrl(storageUrl)
        .build();
  }

  @Bean("webClientStorage")
  @Autowired
  WebClient storageWebClient(final StorageClientProperties properties) {
    return WebClient
        .builder()
        .baseUrl(properties.getStorageUrl())
        .build();
  }

}
