package com.octoperf.kraken.runtime.client.spring;

import com.octoperf.kraken.runtime.backend.api.TaskListService;
import com.octoperf.kraken.runtime.client.api.RuntimeWatchClient;
import com.octoperf.kraken.runtime.client.api.RuntimeWatchClientBuilder;
import com.octoperf.kraken.runtime.logs.LogsService;
import com.octoperf.kraken.security.authentication.api.UserProviderFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.authentication.client.spring.SpringAuthenticatedClientBuilder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SpringRuntimeWatchClientBuilder extends SpringAuthenticatedClientBuilder<RuntimeWatchClient> implements RuntimeWatchClientBuilder {

  LogsService logsService;
  TaskListService taskListService;

  public SpringRuntimeWatchClientBuilder(final List<UserProviderFactory> userProviderFactories,
                                         @NonNull LogsService logsService,
                                         @NonNull TaskListService taskListService) {
    super(userProviderFactories);
    this.logsService = logsService;
    this.taskListService = taskListService;
  }

  @Override
  public Mono<RuntimeWatchClient> build(final AuthenticatedClientBuildOrder order) {
    return super.getOwner(order).map(owner -> new SpringRuntimeWatchClient(owner, logsService, taskListService));
  }

}