package com.octoperf.kraken.runtime.tasks.configuration;

import com.octoperf.kraken.config.runtime.server.api.RuntimeServerProperties;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.tasks.configuration.entity.TaskConfigurationTest;
import com.octoperf.kraken.runtime.tasks.configuration.entity.TasksConfiguration;
import com.octoperf.kraken.runtime.tasks.configuration.entity.TasksConfigurationTest;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.entity.functions.api.OwnerToApplicationId;
import com.octoperf.kraken.security.entity.owner.PublicOwner;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SpringTaskConfigurationServiceTest {

  @Mock
  StorageClientBuilder storageClientBuilder;
  @Mock
  StorageClient storageClient;
  @Mock(lenient = true)
  RuntimeServerProperties serverProperties;
  @Mock
  OwnerToApplicationId toApplicationId;

  SpringTaskConfigurationService service;

  @BeforeEach
  public void before() {
    given(toApplicationId.apply(any())).willReturn(Optional.of("app"));
    given(storageClientBuilder.applicationId(any())).willReturn(storageClientBuilder);
    given(storageClientBuilder.mode(AuthenticationMode.SESSION)).willReturn(storageClientBuilder);
    given(storageClientBuilder.build()).willReturn(Mono.just(storageClient));
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
