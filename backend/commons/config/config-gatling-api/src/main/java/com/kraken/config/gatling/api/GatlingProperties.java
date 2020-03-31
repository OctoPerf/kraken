package com.kraken.config.gatling.api;

import com.kraken.config.api.KrakenProperties;
import com.kraken.config.api.LocalRemoteProperties;

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
