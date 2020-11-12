package com.octoperf.kraken.config.gatling.spring;

import com.octoperf.kraken.config.gatling.api.GatlingScenario;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

import static java.util.Optional.ofNullable;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
final class GatlingScenarioProps implements GatlingScenario {

  static final GatlingScenarioProps DEFAULT_SETUP = builder().build();

  Long concurrentUsers;
  Duration rampUpDuration;
  Duration peakDuration;
  Boolean customSetup;

  GatlingScenarioProps(
      final Long concurrentUsers,
      final Duration rampUpDuration,
      final Duration peakDuration,
      final Boolean customSetup) {
    super();
    this.concurrentUsers = ofNullable(concurrentUsers).orElse(0L);
    this.rampUpDuration = ofNullable(rampUpDuration).orElse(Duration.ZERO);
    this.peakDuration = ofNullable(peakDuration).orElse(Duration.ZERO);
    this.customSetup = ofNullable(customSetup).orElse(false);
  }
}