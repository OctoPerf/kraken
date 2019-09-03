package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.ContainerService;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.Log;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerService implements ContainerService {

  @NonNull CommandService commandService;

  @NonNull Function<String, Container> stringToContainer;

  @NonNull Function<GroupedFlux<String, Container>, Mono<Task>> containersToTask;

  @Override
  public void execute(TaskType taskType, Map<String, String> environment) {
// TODO Run docker-compose up
  }

  @Override
  public Flux<Task> list() {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken.taskId",
            "--filter", "label=com.kraken.containerId",
            "--filter", "label=com.kraken.name",
            "--filter", "status=running",
            "--format", StringToContainer.FORMAT))
        .environment(ImmutableMap.of())
        .build();

    return commandService.execute(command).map(stringToContainer).log().groupBy(Container::getTaskId).flatMap(containersToTask);
  }

//  private Flux<Container> containers(){
//    final var command = Command.builder()
//        .path(".")
//        .command(Arrays.asList("docker",
//            "ps",
//            "--filter", "label=com.kraken.taskId",
//            "--filter", "label=com.kraken.containerId",
//            "--filter", "label=com.kraken.name",
//            "--filter", "status=running",
//            "--format", "\"{{.ID}};{{.Names}};{{.Label \"com.kraken.taskId\"}};{{.Label \"com.kraken.containerId\"}};{{.Label \"com.kraken.name\"}}\""))
//        .environment(ImmutableMap.of())
//        .build();
////    return commandService.execute(command).map(s -> Container.builder().build())
//  }

  @Override
  public Flux<List<Task>> tasks() {
//    TODO Periodical call to list
    return null;
  }

  @Override
  public Flux<Log> logs() {
    return null;
  }

  @Override
  public void attachLogs(String id) {

  }

  @Override
  public void detachLogs(String id) {

  }
}
