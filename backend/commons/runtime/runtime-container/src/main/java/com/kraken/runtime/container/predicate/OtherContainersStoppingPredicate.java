package com.kraken.runtime.container.predicate;

import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
final class OtherContainersStoppingPredicate implements TaskPredicate {

  @NonNull RuntimeContainerProperties containerProperties;

  @Override
  public boolean test(Task task) {
    return task.getId().equals(containerProperties.getTaskId())
        && task.getContainers()
        .stream()
        .allMatch(container -> container.getHostname().equals(containerProperties.getHostname()) // Same group
            && (container.getId().equals(containerProperties.getContainerId()) // Me
            || container.getStatus().ordinal() > ContainerStatus.RUNNING.ordinal()) // Or finished running
        );
  }
}
