package com.kraken.runtime.tasks.configuration.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.tests.utils.ResourceUtils;
import com.kraken.tools.configuration.jackson.JacksonConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;


import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
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
