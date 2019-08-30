package com.kraken.runtime.docker.executor;

import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerConfig;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class RunTaskExecutor implements TaskExecutor {

  @NonNull
  DockerClient client;

  @Override
  public void accept(final Task task) {
    final var config = ContainerConfig.builder().build();
//    client.createContainer();

  }

  @Override
  public boolean test(final Task task) {
    return TaskType.RUN == task.getType();
  }
}
