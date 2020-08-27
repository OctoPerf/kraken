package com.octoperf.kraken.config.runtime.container.spring;

import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties("kraken.container")
final class SpringContainerProperties implements ContainerProperties {
  @NonNull String name;
  @NonNull String hostId;
  @NonNull String taskId;
  @NonNull TaskType taskType;
  @NonNull String applicationId;
  @NonNull String projectId;
  @NonNull String userId;
}
