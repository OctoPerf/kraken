package com.kraken.runtime.docker.env;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class RuntimeUrlPublisher implements EnvironmentPublisher {

  //  TODO FUUUUUUUUUUUUUUUUUUUUUUUUUuuuuuuuuuuuuuuuuuuuuuuuuu faut extraire les runtime-client-properties
//  @NonNull RuntimeClientProperties properties;

  @Override
  public boolean test(TaskType taskType) {
    return true;
  }

  @Override
  public Map<String, String> get() {
    return ImmutableMap.of("KRAKEN_RUNTIME_URL", "");
  }
}
