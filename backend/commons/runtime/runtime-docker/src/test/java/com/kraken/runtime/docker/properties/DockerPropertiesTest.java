package com.kraken.runtime.docker.properties;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.TaskType;
import com.kraken.test.utils.TestUtils;
import com.kraken.tools.configuration.properties.ApplicationProperties;
import org.junit.Test;

import java.nio.file.Paths;
import java.time.Duration;

public class DockerPropertiesTest {

  public static final DockerProperties DOCKER_PROPERTIES = DockerProperties.builder()
      .containersCount(ImmutableMap.of(TaskType.RUN, 2, TaskType.DEBUG, 2, TaskType.RECORD, 2))
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(DOCKER_PROPERTIES);
  }
}
