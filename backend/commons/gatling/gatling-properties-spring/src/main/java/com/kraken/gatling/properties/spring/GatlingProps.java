package com.kraken.gatling.properties.spring;

import com.kraken.gatling.properties.api.GatlingLog;
import com.kraken.gatling.properties.api.GatlingProperties;
import com.kraken.gatling.properties.api.GatlingSimulation;
import com.kraken.tools.obfuscation.ExcludeFromObfuscation;
import com.kraken.tools.properties.api.LocalRemoteProperties;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.nullToEmpty;
import static com.kraken.gatling.properties.spring.GatlingLocalRemoteProp.DEFAULT_LOCAL_REMOTE;
import static com.kraken.gatling.properties.spring.GatlingLogProp.DEFAULT_LOG;
import static com.kraken.gatling.properties.spring.GatlingSimulationProps.DEFAULT_SIMULATION;
import static java.util.Optional.ofNullable;

@Value
@Builder
@ConstructorBinding
@ExcludeFromObfuscation
@ConfigurationProperties("kraken.gatling")
final class GatlingProps implements GatlingProperties {
  String home;
  String bin;
  String description;
  String javaOpts;
  LocalRemoteProperties conf;
  LocalRemoteProperties lib;
  LocalRemoteProperties userFiles;
  LocalRemoteProperties results;
  GatlingLog logs;
  LocalRemoteProperties harPath;
  GatlingSimulation simulation;

  GatlingProps(
    final String home,
    final String bin,
    final String description,
    final String javaOpts,
    final LocalRemoteProperties conf,
    final LocalRemoteProperties lib,
    final LocalRemoteProperties userFiles,
    final LocalRemoteProperties results,
    final GatlingLog logs,
    final LocalRemoteProperties harPath,
    final GatlingSimulation simulation) {
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

