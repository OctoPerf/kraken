package com.kraken.runtime.context.environment;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.environment.ExecutionEnvironment;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.storage.client.properties.StorageClientProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_STORAGE_URL;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class StorageUrlPublisher implements EnvironmentPublisher {

  @NonNull StorageClientProperties properties;

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public Map<String, String> apply(final ExecutionEnvironment context) {
    return ImmutableMap.of(KRAKEN_STORAGE_URL, properties.getStorageUrl());
  }
}
