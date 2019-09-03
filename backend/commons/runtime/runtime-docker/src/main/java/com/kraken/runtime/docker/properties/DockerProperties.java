package com.kraken.runtime.docker.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;
import java.time.Duration;

@Value
@Builder
public class DockerProperties {

  @NonNull
  Duration watchTasksDelay;

}