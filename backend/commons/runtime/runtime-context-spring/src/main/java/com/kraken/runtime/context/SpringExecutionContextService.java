package com.kraken.runtime.context;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.context.api.ExecutionContextService;
import com.kraken.runtime.context.api.MapExecutionEnvironmentEntries;
import com.kraken.runtime.context.api.environment.EnvironmentChecker;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.CancelContext;
import com.kraken.runtime.context.entity.ExecutionContext;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironment;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.runtime.tasks.configuration.TaskConfigurationService;
import com.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import com.kraken.security.entity.owner.Owner;
import com.kraken.template.api.TemplateService;
import com.kraken.tools.unique.id.IdGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.TASK_CONFIGURATION;
import static java.util.stream.Collectors.toUnmodifiableList;

@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringExecutionContextService implements ExecutionContextService {

  @NonNull TaskConfigurationService configurationService;
  @NonNull IdGenerator idGenerator;
  @NonNull List<EnvironmentPublisher> publishers;
  @NonNull List<EnvironmentChecker> checkers;
  @NonNull TemplateService templateService;
  @NonNull MapExecutionEnvironmentEntries toMap;

  @Override
  public Mono<ExecutionContext> newExecuteContext(final Owner owner, final ExecutionEnvironment environment) {
    return configurationService.getConfiguration(owner, environment.getTaskType())
        .map(taskConfiguration -> this.newExecutionContextBuilder(taskConfiguration, owner, environment))
        .flatMap(this::withPublishers)
        .flatMap(this::withTemplate)
        .flatMapMany(this::asMaps)
        .map(t4 -> {
          this.checkers
              .stream()
              .filter(checker -> checker.test(t4.getT2().getTaskType()))
              .forEach(checker -> checker.accept(t4.getT4()));
          return t4;
        })
        .flatMap(this::toExecutionContext)
        .reduce(this::merge);
  }

  @Override
  public Mono<CancelContext> newCancelContext(final Owner owner, final String taskId, final TaskType taskType) {
    return Mono.just(CancelContext.builder()
        .owner(owner)
        .taskId(taskId)
        .taskType(taskType)
        .build());
  }

  private ExecutionContext merge(final ExecutionContext c1, final ExecutionContext c2) {
    return ExecutionContext.builder()
        .owner(c1.getOwner())
        .taskId(c1.getTaskId())
        .taskType(c1.getTaskType())
        .description(c1.getDescription())
        .templates(ImmutableMap.<String, String>builder().putAll(c1.getTemplates()).putAll(c2.getTemplates()).build())
        .build();
  }

  private Mono<ExecutionContext> toExecutionContext(final Tuple4<String, ExecutionContextBuilder, String, Map<String, String>> t4) {
    final var template = t4.getT1();
    final var context = t4.getT2();
    final var hostId = t4.getT3();
    final var envMap = t4.getT4();
    return templateService.replaceAll(template, envMap).map(replaced -> ExecutionContext.builder()
        .owner(context.getOwner())
        .taskId(context.getTaskId())
        .taskType(context.getTaskType())
        .description(context.getDescription())
        .templates(ImmutableMap.of(hostId, replaced))
        .build());
  }

  private Flux<Tuple4<String, ExecutionContextBuilder, String, Map<String, String>>> asMaps(final Tuple2<String, ExecutionContextBuilder> t2) {
    final var template = t2.getT1();
    final var context = t2.getT2();

    return Flux.fromIterable(context.getHostIds())
        .map(hostId -> Tuples.of(template, context, hostId, toMap.apply(hostId, context.getEntries())));
  }

  private Mono<Tuple2<String, ExecutionContextBuilder>> withTemplate(final ExecutionContextBuilder context) {
    final var templateMono = configurationService.getTemplate(context.getOwner(), context.getFile());
    return templateMono.map(template -> Tuples.of(template, context));
  }

  private Mono<ExecutionContextBuilder> withPublishers(final ExecutionContextBuilder context) {
    return Flux
        .fromIterable(this.publishers)
        .filter(publisher -> publisher.test(context.getTaskType()))
        .flatMap(environmentPublisher -> environmentPublisher.apply(context))
        .reduce(context, ExecutionContextBuilder::addEntries);
  }

  private ExecutionContextBuilder newExecutionContextBuilder(final TaskConfiguration taskConfiguration, final Owner owner, final ExecutionEnvironment environment) {
    final var taskId = idGenerator.generate();
    return ExecutionContextBuilder.builder()
        .taskId(taskId)
        .owner(owner)
        .description(environment.getDescription())
        .taskType(environment.getTaskType())
        .file(taskConfiguration.getFile())
        .containersCount(taskConfiguration.getContainersCount() * environment.getHostIds().size())
        .hostIds(environment.getHostIds())
        .entries(environment.getEntries())
        .build()
        .addEntries(taskConfiguration.getEnvironment().entrySet()
            .stream()
            .map(entry -> ExecutionEnvironmentEntry.builder().scope("").from(TASK_CONFIGURATION)
                .key(entry.getKey()).value(entry.getValue()).build())
            .collect(toUnmodifiableList()));
  }

}
