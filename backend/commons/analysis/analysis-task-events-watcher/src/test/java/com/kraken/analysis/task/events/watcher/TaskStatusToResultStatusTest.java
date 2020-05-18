package com.kraken.analysis.task.events.watcher;

import com.kraken.analysis.entity.ResultStatus;
import com.kraken.runtime.entity.task.ContainerStatus;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskStatusToResultStatusTest {

  private final TaskStatusToResultStatus converter = new TaskStatusToResultStatus();

  @Test
  public void shouldConvert() {
    assertThat(converter.apply(ContainerStatus.CREATING)).isEqualTo(ResultStatus.STARTING);
    assertThat(converter.apply(ContainerStatus.STARTING)).isEqualTo(ResultStatus.STARTING);
    assertThat(converter.apply(ContainerStatus.READY)).isEqualTo(ResultStatus.STARTING);
    assertThat(converter.apply(ContainerStatus.RUNNING)).isEqualTo(ResultStatus.RUNNING);
    assertThat(converter.apply(ContainerStatus.STOPPING)).isEqualTo(ResultStatus.STOPPING);
    assertThat(converter.apply(ContainerStatus.DONE)).isEqualTo(ResultStatus.COMPLETED);
    assertThat(converter.apply(ContainerStatus.FAILED)).isEqualTo(ResultStatus.FAILED);
  }

  @Test
  public void shouldConvertAll() {
    Arrays.asList(ContainerStatus.values()).forEach(status -> {
      assertThat(converter.apply(status)).isNotNull();
    });
  }
}
