package com.kraken.config.gatling.spring;

import com.kraken.config.gatling.api.GatlingSimulation;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
final class GatlingSimulationProps implements GatlingSimulation {
  static final GatlingSimulationProps DEFAULT_SIMULATION = builder().build();

  String name;
  String className;
  String packageName;

  GatlingSimulationProps(
    final String name,
    final String className,
    final String packageName) {
    super();
    this.name = nullToEmpty(name);
    this.className = nullToEmpty(className);
    this.packageName = nullToEmpty(packageName);
  }
}