package com.kraken.runtie.server.properties;

import com.kraken.runtime.server.properties.RuntimeServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RuntimeServerPropertiesTestConfiguration {

  @Bean
  RuntimeServerProperties properties() {
    return RuntimeServerPropertiesTest.RUNTIME_SERVER_PROPERTIES;
  }

}
