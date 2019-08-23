package com.kraken.tools.configuration.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Slf4j
@Configuration
public class ApplicationPropertiesConfiguration {

  @Autowired
  @Bean
  ApplicationProperties applicationProperties(@Value("${kraken.data:#{environment.KRAKEN_DATA}}") final String data,
                                              @Value("${kraken.host.data:#{environment.KRAKEN_HOST_DATA}}") final String hostData,
                                              @Value("${kraken.host.uid:#{environment.KRAKEN_HOST_UID}}") final String hostUId,
                                              @Value("${kraken.host.gid:#{environment.KRAKEN_HOST_GID}}") final String hostGId) {
    final var dataPath = Path.of(data);
    log.info("Data location is set to " + dataPath.toAbsolutePath());
    log.info("Host Data location is set to " + hostData);
    log.info("Host UID location is set to " + hostUId);
    log.info("Host GID location is set to " + hostGId);

    return ApplicationProperties.builder()
        .data(dataPath)
        .hostData(hostData)
        .hostUId(hostUId)
        .hostGId(hostGId)
        .build();
  }

}
