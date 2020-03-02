package com.kraken.runtime.tasks.configuration;

import com.kraken.runtie.server.properties.RuntimeServerPropertiesTest;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
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

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SpringTaskConfigurationServiceTest {
  @Mock
  StorageClient storageClient;

  RuntimeServerProperties serverProperties;

  SpringTaskConfigurationService service;

  @Before
  public void before(){
    serverProperties = RuntimeServerPropertiesTest.RUNTIME_SERVER_PROPERTIES;
    service = new SpringTaskConfigurationService(serverProperties, storageClient);
  }

  @Test
  public void shouldReturnTasksConfiguration() throws IOException {
    given(storageClient.getYamlContent(serverProperties.getConfigurationPath(), TasksConfiguration.class)).willReturn(Mono.just(TasksConfigurationTest.TASKS_CONFIGURATION));
    final var result = service.getConfiguration("RUN").block();
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(TaskConfigurationTest.TASK_CONFIGURATION);

  }

}
