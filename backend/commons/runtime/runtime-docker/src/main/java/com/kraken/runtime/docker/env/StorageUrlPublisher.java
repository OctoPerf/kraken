package com.kraken.runtime.docker.env;

import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.client.properties.AnalysisClientProperties;
import com.kraken.runtime.entity.TaskType;
import com.kraken.storage.client.properties.StorageClientProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

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
  public Map<String, String> get() {
    return ImmutableMap.of("KRAKEN_STORAGE_URL", properties.getStorageUrl());
  }
}
