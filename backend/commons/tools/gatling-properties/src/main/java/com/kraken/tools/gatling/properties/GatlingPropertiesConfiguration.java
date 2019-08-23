package com.kraken.tools.gatling.properties;

import com.kraken.tools.configuration.properties.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GatlingPropertiesConfiguration {

  @Autowired
  @Bean
  GatlingProperties gatlingProperties(final ApplicationProperties applicationProperties,
                                      @Value("${kraken.gatling.results.root:#{environment.KRAKEN_GATLING_RESULTS_ROOT}}") final String resultsRoot,
                                      @Value("${kraken.version:#{environment.KRAKEN_VERSION}}") final String version) {
    final var root = applicationProperties.getData().resolve(resultsRoot).toString();
    log.info("Results root is set to " + root);
    log.info("Version is set to " + version);

    return GatlingProperties.builder()
        .resultsRoot(root)
        .version(version)
        .build();
  }
}
