package com.kraken.runtime.kubernetes.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class KubernetesProperties {

  @NonNull String namespace;

}
