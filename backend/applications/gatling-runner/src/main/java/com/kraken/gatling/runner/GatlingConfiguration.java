package com.kraken.gatling.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import java.nio.file.Paths;

import static java.util.Optional.ofNullable;

@Slf4j
@Configuration
class GatlingConfiguration {

  @Autowired
  @Bean
  GatlingProperties gatlingProperties(
      @Value("${kraken.gatling.home:#{environment.KRAKEN_GATLING_HOME}}") final String home,
      @Value("${kraken.gatling.conf.local:#{environment.KRAKEN_GATLING_CONF_LOCAL}}") final String localConf,
      @Nullable @Value("${kraken.gatling.conf.remote:#{environment.KRAKEN_GATLING_CONF_REMOTE}}") final String remoteConf,
      @Value("${kraken.gatling.user-files.local:#{environment.KRAKEN_GATLING_USER_FILES_LOCAL}}") final String localUserFiles,
      @Nullable @Value("${kraken.gatling.user-files.remote:#{environment.KRAKEN_GATLING_USER_FILES_REMOTE}}") final String remoteUserFiles
  ) {

    final var homePath = Paths.get(home);

    final var properties = GatlingProperties.builder()
        .gatlingHome(homePath)
        .localConf(homePath.resolve(localConf))
        .remoteConf(ofNullable(remoteConf))
        .localUserFiles(homePath.resolve(localUserFiles))
        .remoteConf(ofNullable(remoteUserFiles))
        .build();

    log.info(properties.toString());

    return properties;
  }


}

