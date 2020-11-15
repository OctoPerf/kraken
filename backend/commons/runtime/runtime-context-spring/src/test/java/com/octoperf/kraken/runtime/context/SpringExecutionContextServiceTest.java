package com.octoperf.kraken.runtime.context;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.context.api.MapExecutionEnvironmentEntries;
import com.octoperf.kraken.runtime.context.api.environment.EnvironmentChecker;
import com.octoperf.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.octoperf.kraken.runtime.context.entity.CancelContext;
import com.octoperf.kraken.runtime.context.entity.ExecutionContext;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironment;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentTest;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.tasks.configuration.TaskConfigurationService;
import com.octoperf.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import com.octoperf.kraken.runtime.tasks.configuration.entity.TaskConfigurationTest;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.template.api.TemplateService;
import com.octoperf.kraken.tools.unique.id.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SpringExecutionContextServiceTest {

  @Mock
  TaskConfigurationService configurationService;
  @Mock
  IdGenerator idGenerator;
  @Mock
  EnvironmentPublisher publisher;
  @Mock
  EnvironmentChecker checker;
  @Mock
  StorageClient storageClient;
  @Mock
  TemplateService templateService;
  @Mock
  MapExecutionEnvironmentEntries toMap;

  private ExecutionEnvironment executionEnvironment;
  private TaskConfiguration taskConfiguration;

  private SpringExecutionContextService service;

  @BeforeEach
  public void before() {
    executionEnvironment = ExecutionEnvironmentTest.EXECUTION_ENVIRONMENT;
    taskConfiguration = TaskConfigurationTest.TASK_CONFIGURATION;
    this.service = new SpringExecutionContextService(
        configurationService,
        idGenerator,
        ImmutableList.of(publisher),
        ImmutableList.of(checker),
        templateService,
        toMap
    );
  }

  @Test
  public void shouldCreateExecutionContext() {
    given(configurationService.getConfiguration(any(), any())).willReturn(Mono.just(taskConfiguration));
    given(configurationService.getTemplate(any(), any())).willReturn(Mono.just("template"));
    given(idGenerator.generate()).willReturn("taskId");
    given(publisher.test(any())).willReturn(true);
    given(publisher.apply(any())).willReturn(Mono.just(ImmutableList.of(
        ExecutionEnvironmentEntry.builder()
            .key("hello")
            .value("world")
            .scope("")
            .from(BACKEND)
            .build()
    )));
    given(checker.test(any())).willReturn(true);
    given(templateService.replaceAll(anyString(), any())).willReturn(Mono.just("replaced"));
    given(toMap.apply(anyString(), any())).willReturn(ImmutableMap.of("hello", "toMap"));

    final var context = this.service.newExecuteContext(Owner.PUBLIC, executionEnvironment).block();
    assertThat(context).isEqualTo(ExecutionContext.builder()
        .owner(Owner.PUBLIC)
        .taskType(TaskType.GATLING_RUN)
        .taskId("taskId")
        .templates(ImmutableMap.of("hostId", "replaced", "other", "replaced"))
        .description("description")
        .build());
    verify(publisher).apply(ExecutionContextBuilder.builder()
        .hostIds(executionEnvironment.getHostIds())
        .owner(Owner.PUBLIC)
        .containersCount(taskConfiguration.getContainersCount() * executionEnvironment.getHostIds().size())
        .taskId("taskId")
        .taskType(executionEnvironment.getTaskType())
        .description(executionEnvironment.getDescription())
        .hostIds(executionEnvironment.getHostIds())
        .file(taskConfiguration.getFile())
        .entries(ImmutableList.<ExecutionEnvironmentEntry>builder().addAll(executionEnvironment.getEntries())
            .add(ExecutionEnvironmentEntry.builder().scope("").from(TASK_CONFIGURATION).key("key").value("value").build()
            ).build()).build());
    verify(toMap).apply("hostId", ImmutableList.of(
        ExecutionEnvironmentEntry.builder().scope("").from(USER).key("KRAKEN_VERSION").value("bar").build(),
        ExecutionEnvironmentEntry.builder().scope("").from(TASK_CONFIGURATION).key("key").value("value").build(),
        ExecutionEnvironmentEntry.builder().scope("").from(BACKEND).key("hello").value("world").build()
    ));
    verify(toMap).apply("other", ImmutableList.of(
        ExecutionEnvironmentEntry.builder().scope("").from(USER).key("KRAKEN_VERSION").value("bar").build(),
        ExecutionEnvironmentEntry.builder().scope("").from(TASK_CONFIGURATION).key("key").value("value").build(),
        ExecutionEnvironmentEntry.builder().scope("").from(BACKEND).key("hello").value("world").build()
    ));
    verify(templateService, times(2)).replaceAll("template", ImmutableMap.of("hello", "toMap"));
  }

  @Test
  public void shouldCreateCancelContext() {
    final var context = this.service.newCancelContext(Owner.PUBLIC, "taskId", TaskType.GATLING_RUN).block();
    assertThat(context).isEqualTo(CancelContext.builder()
        .owner(Owner.PUBLIC)
        .taskType(TaskType.GATLING_RUN)
        .taskId("taskId")
        .build());
  }

}
