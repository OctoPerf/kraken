package com.kraken.runtime.client;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
class RuntimeClientProperties {

  @NonNull
  String runtimeUrl;

}
