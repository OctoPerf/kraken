package com.kraken.runtime.docker.env;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class KrakenVersionPublisher implements EnvironmentPublisher {

  @NonNull RuntimeServerProperties runtimeServerProperties;

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public Map<String, String> get() {
    return ImmutableMap.of("KRAKEN_VERSION", runtimeServerProperties.getVersion());
  }
}
