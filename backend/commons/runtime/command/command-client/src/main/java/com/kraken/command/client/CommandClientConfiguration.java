package com.kraken.command.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
class CommandClientConfiguration {

  @Autowired
  @Bean
  CommandClientProperties commandClientProperties(@Value("${kraken.command.url:#{environment.KRAKEN_COMMAND_URL}}") final String commandUrl) {
    log.info("Command URL is set to " + commandUrl);

    return CommandClientProperties.builder()
        .commandUrl(commandUrl)
        .build();
  }

  @Bean("webClientCommand")
  @Autowired
  WebClient commandWebClient(final CommandClientProperties properties) {
    return WebClient
        .builder()
        .baseUrl(properties.getCommandUrl())
        .build();
  }

}
