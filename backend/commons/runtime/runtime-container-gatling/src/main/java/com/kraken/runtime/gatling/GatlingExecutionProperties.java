package com.kraken.runtime.gatling;

public interface GatlingExecutionProperties {

  String getHome();

  String getBin();

  String getLocalUserFiles();

  String getLocalConf();

  String getLocalLib();

  String getLocalResult();

  String getInfoLog();

  String getDebugLog();

  String getRemoteUserFiles();

  String getRemoteConf();

  String getRemoteLib();

  String getRemoteResult();

  String getLocalHarPath();

  String getRemoteHarPath();

  String getSimulationPackage();

  String getSimulationClass();

  String getSimulation();

  String getDescription();

  String getJavaOpts();
}
