package com.kraken.storage.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.storage.client.properties.StorageClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
class StorageClientConfiguration {

  @Bean("webClientStorage")
  WebClient storageWebClient(final StorageClientProperties properties,
                             final Jackson2JsonDecoder yamlDecoder,
                             final Jackson2JsonEncoder yamlEncoder) {
    return WebClient
        .builder()
        .baseUrl(properties.getStorageUrl())
        .codecs(configurer -> {
          configurer.customCodecs().register(yamlDecoder);
          configurer.customCodecs().register(yamlEncoder);
        })
        .build();
  }

  @Bean("webClientMapper")
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

}
