package com.kraken.runtime.server.properties;

import com.kraken.runtime.entity.TaskType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class RuntimeServerProperties {

  @NonNull
  Map<TaskType, Integer> containersCount;

  @NonNull
  String version;
}