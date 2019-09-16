package com.kraken.telegraf;

import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
final class TaskPredicate implements Predicate<Task> {

  @NonNull RuntimeContainerProperties containerProperties;

  @Override
  public boolean test(Task task) {
    return task.getId().equals(containerProperties.getTaskId())
        && task.getContainers()
        .stream()
        .allMatch(container -> container.getContainerId().equals(containerProperties.getContainerId()) || container.getStatus().ordinal() > ContainerStatus.RUNNING.ordinal());
  }
}
