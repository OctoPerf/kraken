package com.kraken.runtime.gatling;

import com.kraken.runtime.gatling.api.GatlingExecutionProperties;
import com.kraken.runtime.gatling.api.GatlingLocalRemote;
import com.kraken.runtime.gatling.api.GatlingSimulation;
import com.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.annotation.PostConstruct;

@Data
@Slf4j
@ConstructorBinding
@ExcludeFromObfuscation
@ConfigurationProperties("kraken.gatling")
final class GatlingExecutionProps implements GatlingExecutionProperties {
  String home = "";
  String bin = "";
  String description = "";
  String javaOpts = "";
  GatlingLocalRemote conf = new GatlingLocalRemoteProp();
  GatlingLocalRemote lib = new GatlingLocalRemoteProp();
  GatlingLocalRemote userFiles = new GatlingLocalRemoteProp();
  GatlingLocalRemote results = new GatlingLocalRemoteProp();
  GatlingLogProp logs = new GatlingLogProp();
  GatlingLocalRemote harPath = new GatlingLocalRemoteProp();
  GatlingSimulation simulation = new GatlingSimulationProp();

  @PostConstruct
  void log() {
    log.info(toString());
  }
}

