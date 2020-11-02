package com.octoperf.kraken.runtime.client.spring;

import com.octoperf.kraken.runtime.backend.api.TaskListService;
import com.octoperf.kraken.runtime.client.api.RuntimeClient;
import com.octoperf.kraken.runtime.client.api.RuntimeClientBuilder;
import com.octoperf.kraken.runtime.logs.TaskLogsService;
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
final class SpringRuntimeClientBuilder extends SpringAuthenticatedClientBuilder<RuntimeClient> implements RuntimeClientBuilder {

  TaskLogsService logsService;
  TaskListService taskListService;

  public SpringRuntimeClientBuilder(final List<UserProviderFactory> userProviderFactories,
                                    @NonNull TaskLogsService logsService,
                                    @NonNull TaskListService taskListService) {
    super(userProviderFactories);
    this.logsService = logsService;
    this.taskListService = taskListService;
  }

  @Override
  public Mono<RuntimeClient> build(final AuthenticatedClientBuildOrder order) {
    return super.getOwner(order).map(owner -> new SpringRuntimeClient(owner, logsService, taskListService));
  }

}