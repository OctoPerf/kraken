package com.kraken.runtime.server.service;

import com.kraken.analysis.entity.ResultStatus;
import com.kraken.runtime.entity.ContainerStatus;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskStatusToResultStatusTest {

  private final TaskStatusToResultStatus converter = new TaskStatusToResultStatus();

  @Test
  public void shouldConvert() {
    assertThat(converter.apply(ContainerStatus.CREATING)).isEqualTo(ResultStatus.STARTING);
    assertThat(converter.apply(ContainerStatus.STARTING)).isEqualTo(ResultStatus.STARTING);
    assertThat(converter.apply(ContainerStatus.READY)).isEqualTo(ResultStatus.STARTING);
    assertThat(converter.apply(ContainerStatus.RUNNING)).isEqualTo(ResultStatus.RUNNING);
    assertThat(converter.apply(ContainerStatus.DONE)).isEqualTo(ResultStatus.COMPLETED);
  }

}
