package com.octoperf.kraken.runtime.entity.task;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContainerTest {

  public static final Container CONTAINER = Container.builder()
      .id("id")
      .name("name")
      .hostId("hostId")
      .startDate(42L)
      .status(ContainerStatus.STARTING)
      .label("label")
      .build();


  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(CONTAINER);
  }

  @Test
  public void shouldWither() {
    assertThat(CONTAINER.withStatus(ContainerStatus.READY).getStatus()).isEqualTo(ContainerStatus.READY);
  }
}
