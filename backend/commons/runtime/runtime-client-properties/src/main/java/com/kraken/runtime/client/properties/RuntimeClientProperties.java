package com.kraken.runtime.client.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class RuntimeClientProperties {

  @NonNull
  String runtimeUrl;

}
