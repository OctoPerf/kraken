package com.kraken.runtime.entity.task;

import com.kraken.runtime.entity.task.ContainerStatus;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ContainerStatusTest {

  @Test
  public void shouldParse1() {
    Assertions.assertThat(ContainerStatus.parse("some-random-string_CREATING")).isEqualTo(ContainerStatus.CREATING);
  }

  @Test
  public void shouldParse2() {
    Assertions.assertThat(ContainerStatus.parse("some_random_string_DONE")).isEqualTo(ContainerStatus.DONE);
  }

}
