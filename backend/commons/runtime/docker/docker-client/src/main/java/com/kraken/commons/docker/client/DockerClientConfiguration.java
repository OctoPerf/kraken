package com.kraken.commons.docker.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
class DockerClientConfiguration {

  @Autowired
  @Bean
  DockerClientProperties dockerClientProperties(@Value("${kraken.docker.url:#{environment.KRAKEN_DOCKER_URL}}") final String dockerUrl) {
    log.info("Docker URL is set to " + dockerUrl);

    return DockerClientProperties.builder()
        .dockerUrl(dockerUrl)
        .build();
  }

  @Bean("webClientDockerContainer")
  @Autowired
  WebClient dockerContainerWebClient(final DockerClientProperties properties) {
    return WebClient
        .builder()
        .baseUrl(properties.getDockerUrl())
        .build();
  }

  @Bean("webClientDockerImage")
  @Autowired
  WebClient dockerImageWebClient(final DockerClientProperties properties) {
    return WebClient
        .builder()
        .baseUrl(properties.getDockerUrl())
        .build();
  }
}
