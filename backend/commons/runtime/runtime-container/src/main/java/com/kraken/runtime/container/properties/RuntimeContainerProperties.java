package com.kraken.runtime.container.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class RuntimeContainerProperties {

  @NonNull String taskId;
  @NonNull String containerId;

}
