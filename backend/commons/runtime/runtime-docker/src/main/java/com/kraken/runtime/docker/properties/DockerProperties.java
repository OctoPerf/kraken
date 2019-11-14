package com.kraken.runtime.docker.properties;

import com.kraken.runtime.entity.TaskType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Duration;
import java.util.Map;

@Value
@Builder
public class DockerProperties {

  @NonNull
  Map<TaskType, Integer> containersCount;

}