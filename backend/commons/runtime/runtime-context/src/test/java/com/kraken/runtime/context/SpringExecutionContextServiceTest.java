package com.kraken.runtime.context;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.context.api.MapExecutionEnvironmentEntries;
import com.kraken.runtime.context.api.environment.EnvironmentChecker;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.*;
import com.kraken.runtime.entity.environment.ExecutionEnvironment;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentTest;
import com.kraken.runtime.entity.task.Task;
import com.kraken.runtime.tasks.configuration.TaskConfigurationService;
import com.kraken.runtime.tasks.configuration.entity.TaskConfigurationTest;
import com.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import com.kraken.storage.client.StorageClient;
import com.kraken.template.api.TemplateService;
import com.kraken.tools.unique.id.IdGenerator;
import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
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

  private ExecutionContextBuilder contextBuilder;
  private ExecutionEnvironment executionEnvironment;
  private TaskConfiguration taskConfiguration;

  private SpringExecutionContextService service;

  @Before
  public void before() {
    contextBuilder = ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER;
    executionEnvironment = ExecutionEnvironmentTest.EXECUTION_ENVIRONMENT;
    taskConfiguration = TaskConfigurationTest.TASK_CONFIGURATION;
    given(configurationService.getConfiguration(anyString())).willReturn(Mono.just(taskConfiguration));
    given(idGenerator.generate()).willReturn("taskId");
    given(publisher.test(anyString())).willReturn(true);
    given(publisher.apply(any())).willReturn(contextBuilder.addEntries(ImmutableList.of(
        ExecutionEnvironmentEntry.builder()
            .key("hello")
            .value("world")
            .scope("")
            .from(BACKEND)
            .build()
    )));
    given(checker.test(anyString())).willReturn(true);
    given(storageClient.getContent(anyString())).willReturn(Mono.just("template"));
    given(templateService.replaceAll(anyString(), any())).willReturn(Mono.just("replaced"));
    given(toMap.apply(anyString(), any())).willReturn(ImmutableMap.of("hello", "toMap"));
    this.service = new SpringExecutionContextService(
        configurationService,
        idGenerator,
        ImmutableList.of(publisher),
        ImmutableList.of(checker),
        storageClient,
        templateService,
        toMap
    );
  }

  @Test
  public void shouldCreateExecutionContext() {
    final var context = this.service.newExecuteContext("applicationId", executionEnvironment).block();
    assertThat(context).isEqualTo(ExecutionContext.builder()
        .applicationId("applicationId")
        .taskType("RUN")
        .taskId("taskId")
        .templates(ImmutableMap.of("hostId", "replaced", "other", "replaced"))
        .build());
    verify(publisher).apply(ExecutionContextBuilder.builder()
        .hostIds(executionEnvironment.getHostIds())
        .applicationId("applicationId")
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
        ExecutionEnvironmentEntry.builder().scope("").from(USER).key("foo").value("bar").build(),
        ExecutionEnvironmentEntry.builder().scope("").from(BACKEND).key("hello").value("world").build()
    ));
    verify(templateService, times(2)).replaceAll("template", ImmutableMap.of("hello", "toMap"));
  }

  @Test
  public void shouldCreateCancelContext() {
    final var context = this.service.newCancelContext("applicationId", "taskId", "RUN").block();
    assertThat(context).isEqualTo(CancelContext.builder()
        .applicationId("applicationId")
        .taskType("RUN")
        .taskId("taskId")
        .template("template")
        .build());
  }

}
