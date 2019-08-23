package com.kraken.docker.client;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class DockerClientPropertiesTest {

  public static final DockerClientProperties DOCKER_PROPERTIES = DockerClientProperties.builder()
      .dockerUrl("dockerUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(DOCKER_PROPERTIES);
  }

}
