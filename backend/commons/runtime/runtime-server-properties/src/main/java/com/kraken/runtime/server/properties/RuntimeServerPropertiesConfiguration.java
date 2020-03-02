package com.kraken.runtime.server.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kraken.tools.environment.KrakenEnvironmentAtValues.$KRAKEN_RUNTIME_CONFIGURATION_PATH;
import static com.kraken.tools.environment.KrakenEnvironmentAtValues.$KRAKEN_VERSION;

@Slf4j
@Configuration
public class RuntimeServerPropertiesConfiguration {

  @Autowired
  @Bean
  RuntimeServerProperties runtimeServerProperties(@Value($KRAKEN_RUNTIME_CONFIGURATION_PATH) final String configurationPath,
                                                  @Value($KRAKEN_VERSION) final String version) {

    return RuntimeServerProperties.builder()
        .configurationPath(configurationPath)
        .version(version)
        .build();
  }

}
