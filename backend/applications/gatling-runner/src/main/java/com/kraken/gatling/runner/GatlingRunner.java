package com.kraken.gatling.runner;

import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.storage.client.StorageClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static java.util.Optional.of;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class GatlingRunner {

  @NonNull StorageClient storageClient;
  @NonNull RuntimeClient runtimeClient;
  @NonNull CommandService commandService;
  @NonNull RuntimeContainerProperties containerProperties;
  @NonNull GatlingProperties gatlingProperties;

  @PostConstruct
  public void init() {
    final var setStatusStarting = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.STARTING);
    final var downloadConfiguration = storageClient.downloadFolder(gatlingProperties.getGatlingHome().resolve("conf"), of("gatling/conf"));
    final var downloadUserFiles = storageClient.downloadFolder(gatlingProperties.getGatlingHome().resolve("user-files"), of("gatling/user-files"));
    final var setStatusReady = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.READY);
    final var waitForStatusReady = runtimeClient.waitForStatus(containerProperties.getTaskId(), ContainerStatus.READY);
    final var startGatling = Mono.just("ok");
//    commandService.execute()
    final var setStatusStopping = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.STOPPING);
    final var waitForStatusStopping = runtimeClient.waitForStatus(containerProperties.getTaskId(), ContainerStatus.STOPPING);
    final var uploadResult = storageClient.uploadFile(gatlingProperties.getGatlingHome().resolve("result"), of("gatling/result/taskId"));
    final var setStatusDone = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.DONE);

  }

}
