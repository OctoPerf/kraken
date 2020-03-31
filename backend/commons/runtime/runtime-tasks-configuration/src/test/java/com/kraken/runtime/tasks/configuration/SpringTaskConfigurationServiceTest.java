package com.kraken.runtime.tasks.configuration;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.config.runtime.server.api.RuntimeServerProperties;
import com.kraken.runtime.tasks.configuration.entity.TaskConfigurationTest;
import com.kraken.runtime.tasks.configuration.entity.TasksConfiguration;
import com.kraken.runtime.tasks.configuration.entity.TasksConfigurationTest;
import com.kraken.storage.client.StorageClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpringTaskConfigurationServiceTest {
  @Mock
  StorageClient storageClient;
  @Mock
  RuntimeServerProperties serverProperties;

  SpringTaskConfigurationService service;

  @Before
  public void before() {
    when(serverProperties.getConfigPath()).thenReturn("configPath");
    service = new SpringTaskConfigurationService(serverProperties, storageClient);
  }

  @Test
  public void shouldReturnTasksConfiguration() {
    given(storageClient.getYamlContent(serverProperties.getConfigPath(), TasksConfiguration.class)).willReturn(Mono.just(TasksConfigurationTest.TASKS_CONFIGURATION));
    final var result = service.getConfiguration(TaskType.GATLING_RUN).block();
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(TaskConfigurationTest.TASK_CONFIGURATION);
  }

}
