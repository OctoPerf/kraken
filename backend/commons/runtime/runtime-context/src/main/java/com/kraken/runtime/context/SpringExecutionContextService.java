package com.kraken.runtime.context;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.environment.ExecutionEnvironment;
import com.kraken.runtime.tasks.configuration.TaskConfigurationService;
import com.kraken.tools.unique.id.IdGenerator;
import com.runtime.context.api.ExecutionContextService;
import com.runtime.context.entity.ExecutionContext;
import com.runtime.context.entity.ExecutionContextBuilder;
import com.runtime.context.environment.EnvironmentChecker;
import com.runtime.context.environment.EnvironmentPublisher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SpringExecutionContextService implements ExecutionContextService {

  @NonNull TaskConfigurationService configurationService;
  @NonNull IdGenerator idGenerator;
  @NonNull List<EnvironmentPublisher> publishers;
  @NonNull List<EnvironmentChecker> checkers;

  @Override
  public Mono<ExecutionContext> newExecuteContext(String applicationId, ExecutionEnvironment environment) {
    return configurationService.getConfiguration(environment.getTaskType())
        .flatMap(taskConfiguration -> {
          final var taskId = idGenerator.generate();
          final var context = ExecutionContextBuilder.builder()
              .taskId(taskId)
              .applicationId(applicationId)
              .description(environment.getDescription())
              .taskType(environment.getTaskType())
              .file(taskConfiguration.getFile())
              .hostEnvironments(ImmutableMap.<String, Map<String, String>>builder().build())
              .build();
          return this.addHostEnvironments(context, environment.getHosts())
              .flatMap(ctx -> this.addGlobalEnvironment(ctx, environment.getEnvironment()))
              .flatMap(ctx -> this.addGlobalEnvironment(ctx, taskConfiguration.getEnvironment()))
              .flatMap(this::addPublishers)
              .map(this::checkContext)
              .flatMap(this::toExecutionContext);
        });
  }

  @Override
  public Mono<ExecutionContext> newCancelContext(String applicationId, String taskId, String taskType) {
    return configurationService.getConfiguration(taskType)
        .map(taskConfiguration -> ExecutionContextBuilder.builder()
            .taskId(taskId)
            .applicationId(applicationId)
            .taskType(taskType)
            .file(taskConfiguration.getFile())
            .description("")
            .build())
        .flatMap(this::toExecutionContext);
  }

  private Mono<ExecutionContextBuilder> addHostEnvironments(final ExecutionContextBuilder input, final Map<String, Map<String, String>> hostEnvironments) {
    return Flux
        .fromIterable(hostEnvironments.entrySet())
        .reduce(input, (context, hostEntry) -> Flux
            .fromIterable(hostEntry.getValue().entrySet())
            .reduce(context, (curContext, entry) -> curContext.withHostEnvironmentVariable(hostEntry.getKey(), entry.getKey(), entry.getValue()))
            .block());
  }

  private Mono<ExecutionContextBuilder> addGlobalEnvironment(final ExecutionContextBuilder input, final Map<String, String> environment) {
    return Flux
        .fromIterable(environment.entrySet())
        .reduce(input, (context, entry) -> context.withGlobalEnvironmentVariable(entry.getKey(), entry.getValue()));
  }

  private Mono<ExecutionContextBuilder> addPublishers(final ExecutionContextBuilder input) {
    return Flux
        .fromIterable(this.publishers)
        .reduce(input, (context, publisher) -> publisher.apply(context));
  }

  private ExecutionContextBuilder checkContext(final ExecutionContextBuilder input) {
    this.checkers.forEach(environmentChecker -> environmentChecker.accept(input.getHostEnvironments()));
    return input;
  }

  private Mono<ExecutionContext> toExecutionContext(final ExecutionContextBuilder input) {
    return configurationService.getTemplates(input.getFile(), input.getHostEnvironments())
        .map(templates -> ExecutionContext.builder()
            .taskId(input.getTaskId())
            .taskType(input.getTaskType())
            .applicationId(input.getApplicationId())
            .templates(templates)
            .build());
  }

}
