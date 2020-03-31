package com.kraken.config.gatling.spring;

import com.kraken.config.gatling.api.GatlingProperties;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.nullToEmpty;
import static com.kraken.config.gatling.spring.GatlingLocalRemoteProps.DEFAULT_LOCAL_REMOTE;
import static com.kraken.config.gatling.spring.GatlingLogProp.DEFAULT_LOG;
import static com.kraken.config.gatling.spring.GatlingSimulationProps.DEFAULT_SIMULATION;
import static java.util.Optional.ofNullable;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.gatling")
final class GatlingProps implements GatlingProperties {
  String home;
  String bin;
  String description;
  String javaOpts;
  GatlingLocalRemoteProps conf;
  GatlingLocalRemoteProps lib;
  GatlingLocalRemoteProps userFiles;
  GatlingLocalRemoteProps results;
  GatlingLogProp logs;
  GatlingLocalRemoteProps harPath;
  GatlingSimulationProps simulation;

  GatlingProps(
    final String home,
    final String bin,
    final String description,
    final String javaOpts,
    final GatlingLocalRemoteProps conf,
    final GatlingLocalRemoteProps lib,
    final GatlingLocalRemoteProps userFiles,
    final GatlingLocalRemoteProps results,
    final GatlingLogProp logs,
    final GatlingLocalRemoteProps harPath,
    final GatlingSimulationProps simulation) {
    super();
    this.home = nullToEmpty(home);
    this.bin = nullToEmpty(bin);
    this.description = nullToEmpty(description);
    this.javaOpts = nullToEmpty(javaOpts);
    this.conf = ofNullable(conf).orElse(DEFAULT_LOCAL_REMOTE);
    this.lib = ofNullable(lib).orElse(DEFAULT_LOCAL_REMOTE);
    this.userFiles = ofNullable(userFiles).orElse(DEFAULT_LOCAL_REMOTE);
    this.results = ofNullable(results).orElse(DEFAULT_LOCAL_REMOTE);
    this.logs = ofNullable(logs).orElse(DEFAULT_LOG);
    this.harPath = ofNullable(harPath).orElse(DEFAULT_LOCAL_REMOTE);
    this.simulation = ofNullable(simulation).orElse(DEFAULT_SIMULATION);
  }
}

