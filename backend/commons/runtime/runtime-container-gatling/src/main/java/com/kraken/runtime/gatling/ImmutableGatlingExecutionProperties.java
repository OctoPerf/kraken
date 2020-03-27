package com.kraken.runtime.gatling;

import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.nio.file.Paths.get;

@Slf4j
@Builder
@Component
@lombok.Value
@ConstructorBinding
@ExcludeFromObfuscation
@ConfigurationProperties("kraken.gatling")
final class ImmutableGatlingExecutionProperties implements GatlingExecutionProperties {
  RuntimeContainerProperties container;
  String home;
  String bin;
  String localConf;
  String remoteConf;
  String localLib;
  String remoteLib;
  String localUserFiles;
  String remoteUserFiles;
  String localResult;
  String remoteResult;
  String infoLog;
  String debugLog;
  String simulation;
  String description;
  String localHarPath;
  String remoteHarPath;
  String simulationPackage;
  String simulationClass;
  String javaOpts;

  @PostConstruct
  void log() {
    log.info(toString());
  }

  @Override
  public String getRemoteResult() {
    return get(remoteResult).resolve(container.getTaskId()).toString();
  }

  @Override
  public String getRemoteConf() {
    return get(remoteConf).resolve(container.getTaskType().toString()).toString();
  }
}

