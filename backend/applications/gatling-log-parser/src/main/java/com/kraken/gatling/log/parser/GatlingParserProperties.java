package com.kraken.gatling.log.parser;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;

@Value
@Builder
public class GatlingParserProperties {

  @NonNull Path gatlingHome;
  @NonNull Path debugLog;

}

