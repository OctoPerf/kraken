package com.kraken.runtime.kubernetes;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class K8SClientConfiguration {

  @Bean
  ApiClient client() throws IOException {
    final var apiClient = ClientBuilder.defaultClient();
    // https://github.com/kubernetes-client/java/issues/150
    apiClient.getHttpClient().setReadTimeout(24, TimeUnit.HOURS);
    return apiClient;
  }

  @Autowired
  @Bean
  CoreV1Api api(final ApiClient client) throws IOException {
    return new CoreV1Api(client);
  }
}
