package com.kraken.runtime.gatling;

import com.kraken.runtime.gatling.api.GatlingSimulation;
import lombok.Data;

@Data
final class GatlingSimulationProp implements GatlingSimulation {
  String name = "";
  String className = "";
  String packageName = "";
}