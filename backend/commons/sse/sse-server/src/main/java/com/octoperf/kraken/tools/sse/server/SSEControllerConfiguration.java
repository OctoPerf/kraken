package com.octoperf.kraken.tools.sse.server;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
class SSEControllerConfiguration {

  @Bean
  public Map<SSEChannel, SSEChannelBuilder> channelBuilders(@NonNull final List<SSEChannelBuilder> builders) {
    return builders.stream().collect(Collectors.toMap(SSEChannelBuilder::get, builder -> builder));
  }
}
