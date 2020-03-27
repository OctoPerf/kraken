package com.kraken.gatling.properties.api;

public interface GatlingProperties {

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
