package com.kraken.runtime.tasks.configuration.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.tests.utils.ResourceUtils;
import com.kraken.tools.configuration.jackson.JacksonConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JacksonConfiguration.class})
public class TasksConfigurationJacksonTest {

  @Autowired
  @Qualifier("yamlObjectMapper")
  ObjectMapper yamlObjectMapper;

  @Test
  public void shouldReadYaml() throws IOException {
    final var yaml = ResourceUtils.getResourceContent("configuration.yaml");
    final TasksConfiguration tasksConfiguration = yamlObjectMapper.readValue(yaml, TasksConfiguration.class);
    assertThat(tasksConfiguration).isNotNull();
    assertThat(tasksConfiguration.getTasks()).isNotNull();
    assertThat(tasksConfiguration.getTasks().size()).isEqualTo(3);
  }

}
