package com.kraken.runtime.server.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class RuntimeServerProperties {
  @NonNull
  String version;
  @NonNull
  String configurationPath;
}