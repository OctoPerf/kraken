package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.task.TaskType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.container")
final class SpringContainerProperties implements ContainerProperties {
  @NonNull String name;
  @NonNull String hostId;
  @NonNull String taskId;
  @NonNull TaskType taskType;
}
