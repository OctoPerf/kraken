package com.kraken.tools.environment;

public interface KrakenEnvironmentAtValues {
  String $KRAKEN_GATLING_HOME = "${kraken.gatling.home:#{environment.KRAKEN_GATLING_HOME}}";
  String $KRAKEN_GATLING_BIN = "${kraken.gatling.bin:#{environment.KRAKEN_GATLING_BIN}}";
  String $KRAKEN_GATLING_CONF_LOCAL = "${kraken.gatling.conf.local:#{environment.KRAKEN_GATLING_CONF_LOCAL}}";
  String $KRAKEN_GATLING_CONF_REMOTE = "${kraken.gatling.conf.remote:#{environment.KRAKEN_GATLING_CONF_REMOTE}}";
  String $KRAKEN_GATLING_LIB_LOCAL = "${kraken.gatling.lib.local:#{environment.KRAKEN_GATLING_LIB_LOCAL}}";
  String $KRAKEN_GATLING_LIB_REMOTE = "${kraken.gatling.lib.remote:#{environment.KRAKEN_GATLING_LIB_REMOTE}}";
  String $KRAKEN_GATLING_USER_FILES_LOCAL = "${kraken.gatling.user-files.local:#{environment.KRAKEN_GATLING_USER_FILES_LOCAL}}";
  String $KRAKEN_GATLING_USER_FILES_REMOTE = "${kraken.gatling.user-files.remote:#{environment.KRAKEN_GATLING_USER_FILES_REMOTE}}";
  String $KRAKEN_GATLING_RESULT_LOCAL = "${kraken.gatling.result.local:#{environment.KRAKEN_GATLING_RESULT_LOCAL}}";
  String $KRAKEN_GATLING_RESULT_REMOTE = "${kraken.gatling.result.remote:#{environment.KRAKEN_GATLING_RESULT_REMOTE}}";
  String $KRAKEN_GATLING_RESULT_INFO_LOG = "${kraken.gatling.result.log.info:#{environment.KRAKEN_GATLING_RESULT_INFO_LOG}}";
  String $KRAKEN_GATLING_RESULT_DEBUG_LOG = "${kraken.gatling.result.log.debug:#{environment.KRAKEN_GATLING_RESULT_DEBUG_LOG}}";
  String $KRAKEN_GATLING_SIMULATION = "${kraken.gatling.simulation:#{environment.KRAKEN_GATLING_SIMULATION}}";
  String $KRAKEN_GATLING_DESCRIPTION = "${kraken.gatling.description:#{environment.KRAKEN_GATLING_DESCRIPTION}}";
  String $KRAKEN_GATLING_HAR_PATH_LOCAL = "${kraken.gatling.har-path.local:#{environment.KRAKEN_GATLING_HAR_PATH_LOCAL}}";
  String $KRAKEN_GATLING_HAR_PATH_REMOTE = "${kraken.gatling.har-path.remote:#{environment.KRAKEN_GATLING_HAR_PATH_REMOTE}}";
  String $KRAKEN_GATLING_SIMULATION_PACKAGE = "${kraken.gatling.simulation-package:#{environment.KRAKEN_GATLING_SIMULATION_PACKAGE}}";
  String $KRAKEN_GATLING_SIMULATION_CLASS = "${kraken.gatling.simulation-class:#{environment.KRAKEN_GATLING_SIMULATION_CLASS}}";
  String $KRAKEN_GATLING_JAVA_OPTS = "${kraken.gatling.java-opts:#{environment.KRAKEN_GATLING_JAVA_OPTS}}";

  String $KRAKEN_SSE_KEEP_ALIVE_DELAY = "${kraken.sse.keep-alive:#{environment.KRAKEN_SSE_KEEP_ALIVE_DELAY ?: 15}}";
}
