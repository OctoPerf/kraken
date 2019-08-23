package com.kraken.docker.client;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
class DockerClientProperties {

  @NonNull
  String dockerUrl;

}
