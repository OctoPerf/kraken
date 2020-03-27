package com.kraken.gatling.properties.spring;

import com.kraken.gatling.properties.api.GatlingSimulation;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder
@ConstructorBinding
final class GatlingSimulationProps implements GatlingSimulation {
  static final GatlingSimulation DEFAULT_SIMULATION = builder().build();

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