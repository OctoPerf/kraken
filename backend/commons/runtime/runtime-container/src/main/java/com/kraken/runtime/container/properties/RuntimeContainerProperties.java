package com.kraken.runtime.container.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
class RuntimeContainerProperties {

  @NonNull String taskId;
  @NonNull String containerId;

}
