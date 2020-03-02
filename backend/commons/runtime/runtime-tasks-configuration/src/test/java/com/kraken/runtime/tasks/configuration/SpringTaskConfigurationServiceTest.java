package com.kraken.runtime.tasks.configuration;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtie.server.properties.RuntimeServerPropertiesTest;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.runtime.tasks.configuration.entity.TaskConfigurationTest;
import com.kraken.runtime.tasks.configuration.entity.TasksConfiguration;
import com.kraken.runtime.tasks.configuration.entity.TasksConfigurationTest;
import com.kraken.storage.client.StorageClient;
import com.kraken.template.api.TemplateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SpringTaskConfigurationServiceTest {
  @Mock
  StorageClient storageClient;

  @Mock
  TemplateService templateService;

  RuntimeServerProperties serverProperties;

  SpringTaskConfigurationService service;

  @Before
  public void before() {
    serverProperties = RuntimeServerPropertiesTest.RUNTIME_SERVER_PROPERTIES;
    service = new SpringTaskConfigurationService(serverProperties, storageClient, templateService);
  }

  @Test
  public void shouldReturnTasksConfiguration() {
    given(storageClient.getYamlContent(serverProperties.getConfigurationPath(), TasksConfiguration.class)).willReturn(Mono.just(TasksConfigurationTest.TASKS_CONFIGURATION));
    final var result = service.getConfiguration("RUN").block();
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(TaskConfigurationTest.TASK_CONFIGURATION);
  }


  @Test
  public void shouldReturnTemplates() throws IOException {
    final var filePath = "filePath";
    final var templateIn = "templateIn";
    final var hostId1 = "hostId1";
    final var hostId2 = "hostId2";
    final var template1 = "template1";
    final var template2 = "template2";
    final var environment = ImmutableMap.<String, Map<String, String>>of(hostId1, ImmutableMap.of("key", "value"), hostId2, ImmutableMap.of("foo", "bar"));
    given(storageClient.getContent(filePath)).willReturn(Mono.just(templateIn));
    given(templateService.replaceAll(templateIn, environment.get(hostId1))).willReturn(Mono.just(template1));
    given(templateService.replaceAll(templateIn, environment.get(hostId2))).willReturn(Mono.just(template2));
    final var result = service.getTemplates(filePath, environment).block();
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(ImmutableMap.of(hostId1, template1, hostId2, template2));
  }
}
