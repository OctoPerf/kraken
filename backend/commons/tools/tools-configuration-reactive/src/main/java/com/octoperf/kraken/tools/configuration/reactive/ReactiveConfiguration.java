package com.octoperf.kraken.tools.configuration.reactive;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReactiveConfiguration {

  // Force netty usage
  @Bean
  public ReactiveWebServerFactory reactiveWebServerFactory() {
    return new NettyReactiveWebServerFactory();
  }

}
