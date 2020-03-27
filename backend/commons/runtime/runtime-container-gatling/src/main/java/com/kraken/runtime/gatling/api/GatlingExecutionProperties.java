package com.kraken.runtime.gatling.api;

public interface GatlingExecutionProperties {

  String getHome();

  String getBin();

  GatlingLog getLogs();

  GatlingLocalRemote getUserFiles();

  GatlingLocalRemote getConf();

  GatlingLocalRemote getLib();

  GatlingLocalRemote getResults();

  GatlingLocalRemote getHarPath();

  GatlingSimulation getSimulation();

  String getDescription();

  String getJavaOpts();
}
