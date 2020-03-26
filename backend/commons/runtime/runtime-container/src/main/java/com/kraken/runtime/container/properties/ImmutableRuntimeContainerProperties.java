package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.annotation.PostConstruct;

@Slf4j
@Value
@Builder
@ConstructorBinding
@ExcludeFromObfuscation
@ConfigurationProperties("kraken.runtime")
final class ImmutableRuntimeContainerProperties implements RuntimeContainerProperties {
  @NonNull String taskId;
  @NonNull TaskType taskType;
  @NonNull String containerName;
  @NonNull String hostId;

  @PostConstruct
  void postConstruct() {
    log.info(toString());
  }
}
