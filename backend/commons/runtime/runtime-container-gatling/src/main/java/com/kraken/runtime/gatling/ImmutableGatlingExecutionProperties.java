package com.kraken.runtime.gatling;

import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

import static com.google.common.base.Strings.nullToEmpty;
import static com.kraken.tools.environment.KrakenEnvironmentAtValues.*;
import static java.nio.file.Paths.get;

@Slf4j
@Component
@lombok.Value
final class ImmutableGatlingExecutionProperties implements GatlingExecutionProperties {

  Path gatlingHome;
  Path gatlingBin;
  Path localUserFiles;
  Path localConf;
  Path localLib;
  Path localResult;
  Path infoLog;
  Path debugLog;
  String remoteUserFiles;
  String remoteConf;
  String remoteLib;
  String remoteResult;

  Path localHarPath;
  String remoteHarPath;
  String simulationPackage;
  String simulationClass;
  String simulation;
  String description;

  String javaOpts;

  @Builder
  ImmutableGatlingExecutionProperties(
    final RuntimeContainerProperties containerProperties,
    @Value($KRAKEN_GATLING_HOME) final String gatlingHome,
    @Value($KRAKEN_GATLING_BIN) final String gatlingBin,
    @Value($KRAKEN_GATLING_CONF_LOCAL) final String localConf,
    @Value($KRAKEN_GATLING_CONF_REMOTE) final String remoteConf,
    @Value($KRAKEN_GATLING_LIB_LOCAL) final String localLib,
    @Value($KRAKEN_GATLING_LIB_REMOTE) final String remoteLib,
    @Value($KRAKEN_GATLING_USER_FILES_LOCAL) final String localUserFiles,
    @Value($KRAKEN_GATLING_USER_FILES_REMOTE) final String remoteUserFiles,
    @Value($KRAKEN_GATLING_RESULT_LOCAL) final String localResult,
    @Value($KRAKEN_GATLING_RESULT_REMOTE) final String remoteResult,
    @Value($KRAKEN_GATLING_RESULT_INFO_LOG) final String infoLog,
    @Value($KRAKEN_GATLING_RESULT_DEBUG_LOG) final String debugLog,
    @Value($KRAKEN_GATLING_SIMULATION) final String simulation,
    @Value($KRAKEN_GATLING_DESCRIPTION) final String description,
    @Value($KRAKEN_GATLING_HAR_PATH_LOCAL) final String localHarPath,
    @Value($KRAKEN_GATLING_HAR_PATH_REMOTE) final String remoteHarPath,
    @Value($KRAKEN_GATLING_SIMULATION_PACKAGE) final String simulationPackage,
    @Value($KRAKEN_GATLING_SIMULATION_CLASS) final String simulationClass,
    @Value($KRAKEN_GATLING_JAVA_OPTS) final String javaOpts) {
    super();
    this.gatlingHome = get(gatlingHome);
    this.gatlingBin = get(gatlingBin);
    this.localConf = get(localConf);
    this.remoteConf = get(remoteConf).resolve(containerProperties.getTaskType().toString()).toString();
    this.localUserFiles = get(localUserFiles);
    this.remoteUserFiles = nullToEmpty(remoteUserFiles);
    this.localLib = get(localLib);
    this.remoteLib = nullToEmpty(remoteLib);
    this.localResult = get(localResult);
    this.remoteResult = get(remoteResult).resolve(containerProperties.getTaskId()).toString();
    this.infoLog = get(infoLog);
    this.debugLog = get(debugLog);
    this.simulation = nullToEmpty(simulation);
    this.description = nullToEmpty(description);
    this.localHarPath = get(nullToEmpty(localHarPath));
    this.remoteHarPath = nullToEmpty(remoteHarPath);
    this.simulationPackage = nullToEmpty(simulationPackage);
    this.simulationClass = nullToEmpty(simulationClass);
    this.javaOpts = nullToEmpty(javaOpts);
    log.info(toString());
  }
}

