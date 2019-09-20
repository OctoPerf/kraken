package com.kraken.runtime.docker;


import com.kraken.runtime.entity.TaskType;
import com.kraken.tools.configuration.properties.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class TaskTypeToPath implements Function<TaskType, String> {

  @NonNull ApplicationProperties applicationProperties;

  @Override
  public String apply(TaskType taskType) {
    return this.applicationProperties.getData().resolve(taskType.toString()).toString();
  }
}
