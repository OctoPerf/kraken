package com.kraken.commons.docker.client.entity;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class DockerContainerTest {

  public static final DockerContainer DOCKER_CONTAINER = DockerContainer.builder()
      .id("id")
      .image("image")
      .name("name")
      .status("status")
      .full("some string")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(DOCKER_CONTAINER);
  }
}
