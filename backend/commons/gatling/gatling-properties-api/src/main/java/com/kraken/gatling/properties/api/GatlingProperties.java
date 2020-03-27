package com.kraken.gatling.properties.api;

import com.kraken.tools.properties.api.KrakenProperties;
import com.kraken.tools.properties.api.LocalRemoteProperties;

public interface GatlingProperties extends KrakenProperties {

  String getHome();

  String getBin();

  GatlingLog getLogs();

  LocalRemoteProperties getUserFiles();

  LocalRemoteProperties getConf();

  LocalRemoteProperties getLib();

  LocalRemoteProperties getResults();

  LocalRemoteProperties getHarPath();

  GatlingSimulation getSimulation();

  String getDescription();

  String getJavaOpts();
}
