package com.kraken.runtime.tasks.configuration;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.config.runtime.server.api.RuntimeServerProperties;
import com.kraken.runtime.tasks.configuration.entity.TaskConfigurationTest;
import com.kraken.runtime.tasks.configuration.entity.TasksConfiguration;
import com.kraken.runtime.tasks.configuration.entity.TasksConfigurationTest;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.entity.functions.api.OwnerToApplicationId;
import com.kraken.security.entity.owner.PublicOwner;
import com.kraken.storage.client.api.StorageClient;
import com.kraken.storage.client.api.StorageClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpringTaskConfigurationServiceTest {

  @Mock
  StorageClientBuilder storageClientBuilder;
  @Mock
  StorageClient storageClient;
  @Mock
  RuntimeServerProperties serverProperties;
  @Mock
  OwnerToApplicationId toApplicationId;

  SpringTaskConfigurationService service;

  @Before
  public void before() {
    given(toApplicationId.apply(any())).willReturn(Optional.of("app"));
    given(storageClientBuilder.applicationId(any())).willReturn(storageClientBuilder);
    given(storageClientBuilder.mode(AuthenticationMode.SESSION)).willReturn(storageClientBuilder);
    given(storageClientBuilder.build()).willReturn(storageClient);
    given(serverProperties.getConfigPath()).willReturn("configPath");
    service = new SpringTaskConfigurationService(serverProperties, storageClientBuilder, toApplicationId);
  }

  @Test
  public void shouldReturnTasksConfiguration() {
    given(storageClient.getYamlContent(serverProperties.getConfigPath(), TasksConfiguration.class)).willReturn(Mono.just(TasksConfigurationTest.TASKS_CONFIGURATION));
    final var result = service.getConfiguration(PublicOwner.INSTANCE, TaskType.GATLING_RUN).block();
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(TaskConfigurationTest.TASK_CONFIGURATION);
  }

  @Test
  public void shouldReturnTemplate() {
    given(storageClient.getContent("file")).willReturn(Mono.just("content"));
    final var result = service.getTemplate(PublicOwner.INSTANCE, "file").block();
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo("content");
  }

}
