package com.kraken.commons.command.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Disposable;

import java.util.HashMap;
import java.util.Map;

@Configuration
class CommandConfiguration {

  @Bean
  Map<String, Disposable> subscriptionsMap() {
    return new HashMap<>();
  }
}
