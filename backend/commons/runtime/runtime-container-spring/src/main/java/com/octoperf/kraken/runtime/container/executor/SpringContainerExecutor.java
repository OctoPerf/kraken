package com.octoperf.kraken.runtime.container.executor;


import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.runtime.client.api.RuntimeClient;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringContainerExecutor implements ContainerExecutor {

  @NonNull Mono<RuntimeClient> runtimeClientMono;
  @NonNull ContainerProperties properties;

  @Override
  public void execute(Optional<ContainerExecutorStep> setUp,
                      ContainerExecutorStep execute,
                      Optional<ContainerExecutorStep> tearDown) {
    final var client = runtimeClientMono.block();
    final var findMe = client.find(properties.getTaskId(), properties.getName());
    final var me = findMe.block();
    try {
      setUp.ifPresent(consumer -> {
        client.setStatus(me, ContainerStatus.PREPARING).block();
        consumer.accept(client, me);
      });
      client.setStatus(me, ContainerStatus.READY).block();
      client.waitForStatus(me, ContainerStatus.READY).block();
      client.setStatus(me, ContainerStatus.RUNNING).block();
      execute.accept(client, me);
      tearDown.ifPresent(consumer -> {
        client.setStatus(me, ContainerStatus.STOPPING).block();
        client.waitForStatus(me, ContainerStatus.STOPPING).block();
        consumer.accept(client, me);
      });
      client.setStatus(me, ContainerStatus.DONE).block();
    } catch (Exception e) {
      log.error("Error occurred during container execution", e);
      client.setStatus(me, ContainerStatus.FAILED).block();
    }
  }
}
