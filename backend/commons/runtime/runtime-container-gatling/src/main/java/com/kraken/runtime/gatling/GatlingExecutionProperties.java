package com.kraken.runtime.gatling;

import java.nio.file.Path;

public interface GatlingExecutionProperties {

  Path getGatlingHome();

  Path getGatlingBin();

  Path getLocalUserFiles();

  Path getLocalConf();

  Path getLocalLib();

  Path getLocalResult();

  Path getInfoLog();

  Path getDebugLog();

  String getRemoteUserFiles();

  String getRemoteConf();

  String getRemoteLib();

  String getRemoteResult();

  Path getLocalHarPath();

  String getRemoteHarPath();

  String getSimulationPackage();

  String getSimulationClass();

  String getSimulation();

  String getDescription();

  String getJavaOpts();
}
