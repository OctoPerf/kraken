package com.kraken.runtime.gatling;

import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Slf4j
@Configuration
class GatlingExecutionConfiguration {

  @Autowired
  @Bean
  GatlingExecutionProperties gatlingProperties(
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
      @Value("${kraken.gatling.result.log.debug:#{environment.KRAKEN_GATLING_RESULT_DEBUG_LOG}}") final String debugLog,
      @Nullable @Value("${kraken.gatling.simulation:#{environment.KRAKEN_GATLING_SIMULATION}}") final String simulation,
      @Nullable @Value("${kraken.gatling.description:#{environment.KRAKEN_GATLING_DESCRIPTION}}") final String description,
      @Nullable @Value("${kraken.gatling.har-path.local:#{environment.KRAKEN_GATLING_HAR_PATH_LOCAL}}") final String localHarPath,
      @Nullable @Value("${kraken.gatling.har-path.remote:#{environment.KRAKEN_GATLING_HAR_PATH_REMOTE}}") final String remoteHarPath,
      @Nullable @Value("${kraken.gatling.simulation-package:#{environment.KRAKEN_GATLING_SIMULATION_PACKAGE}}") final String simulationPackage,
      @Nullable @Value("${kraken.gatling.simulation-class:#{environment.KRAKEN_GATLING_SIMULATION_CLASS}}") final String simulationClass
  ) {

    final var properties = GatlingExecutionProperties.builder()
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
        .simulation(ofNullable(simulation).orElse(""))
        .description(ofNullable(description).orElse(""))
        .localHarPath(ofNullable(localHarPath).map(Paths::get).orElse(Path.of("")))
        .remoteHarPath(ofNullable(remoteHarPath).orElse(""))
        .simulationPackage(ofNullable(simulationPackage).orElse(""))
        .simulationClass(ofNullable(simulationClass).orElse(""))
        .build();

    log.info(properties.toString());

    return properties;
  }


}

