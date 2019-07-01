package com.kraken.commons.docker.client.entity;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class DockerImageTest {

  public static final DockerImage DOCKER_IMAGE = DockerImage.builder()
      .id("id")
      .name("name")
      .tag("tag")
      .created("timestamp")
      .size(42L)
      .full("some string")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(DOCKER_IMAGE);
  }
}
