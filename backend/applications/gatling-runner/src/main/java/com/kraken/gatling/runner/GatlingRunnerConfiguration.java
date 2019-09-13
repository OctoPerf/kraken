package com.kraken.gatling.runner;

import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

import static java.util.Optional.of;

@Slf4j
@Configuration
class GatlingRunnerConfiguration {

  @Autowired
  @Bean
  GatlingRunnerProperties gatlingProperties(
      RuntimeContainerProperties containerProperties,
      @Value("${kraken.gatling.home:#{environment.KRAKEN_GATLING_HOME}}") final String home,
      @Value("${kraken.gatling.bin:#{environment.KRAKEN_GATLING_BIN}}") final String bin,
      @Value("${kraken.gatling.conf.local:#{environment.KRAKEN_GATLING_CONF_LOCAL}}") final String localConf,
      @Value("${kraken.gatling.conf.remote:#{environment.KRAKEN_GATLING_CONF_REMOTE}}") final String remoteConf,
      @Value("${kraken.gatling.user-files.local:#{environment.KRAKEN_GATLING_USER_FILES_LOCAL}}") final String localUserFiles,
      @Value("${kraken.gatling.user-files.remote:#{environment.KRAKEN_GATLING_USER_FILES_REMOTE}}") final String remoteUserFiles,
      @Value("${kraken.gatling.result.local:#{environment.KRAKEN_GATLING_RESULT_LOCAL}}") final String localResult,
      @Value("${kraken.gatling.result.remote:#{environment.KRAKEN_GATLING_RESULT_REMOTE}}") final String remoteResult,
      @Value("${kraken.gatling.result.log.info:#{environment.KRAKEN_GATLING_RESULT_INFO_LOG}}") final String infoLog,
      @Value("${kraken.gatling.result.log.debug:#{environment.KRAKEN_GATLING_RESULT_DEBUG_LOG}}") final String debugLog
  ) {

    final var properties = GatlingRunnerProperties.builder()
        .gatlingHome(Paths.get(home))
        .gatlingBin(Paths.get(bin))
        .localConf(Paths.get(localConf))
        .remoteConf(of(Paths.get(remoteConf).resolve(containerProperties.getTaskType().toString()).toString()))
        .localUserFiles(Paths.get(localUserFiles))
        .remoteUserFiles(of(remoteUserFiles))
        .localResult(Paths.get(localResult))
        .remoteResult(of(Paths.get(remoteResult).resolve(containerProperties.getTaskId()).toString()))
        .infoLog(Paths.get(infoLog))
        .debugLog(Paths.get(debugLog))
        .build();

    log.info(properties.toString());

    return properties;
  }


}

