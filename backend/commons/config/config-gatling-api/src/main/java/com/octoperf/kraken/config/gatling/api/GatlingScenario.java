package com.octoperf.kraken.config.gatling.api;

import java.time.Duration;

public interface GatlingScenario {

  Long getConcurrentUsers();

  Duration getRampUpDuration();

  Duration getPeakDuration();

  Boolean getCustomSetup();

}
