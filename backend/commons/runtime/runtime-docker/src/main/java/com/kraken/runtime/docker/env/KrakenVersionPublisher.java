package com.kraken.runtime.docker.env;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.TaskType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class KrakenVersionPublisher implements EnvironmentPublisher {

  String version;

  public KrakenVersionPublisher(@Value("${kraken.version:#{environment.KRAKEN_VERSION}}") final String version){
    this.version = Objects.requireNonNull(version);
  }

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public Map<String, String> get() {
    return ImmutableMap.of("KRAKEN_VERSION", version);
  }
}
