package com.octoperf.kraken.analysis.task.events.watcher;

import com.octoperf.kraken.analysis.entity.ResultType;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTypeToResultTypeTest {

  private final TaskTypeToResultType converter = new TaskTypeToResultType();

  @Test
  public void shouldConvert() {
    assertThat(converter.apply(TaskType.GATLING_RUN)).isEqualTo(ResultType.RUN);
    assertThat(converter.apply(TaskType.GATLING_DEBUG)).isEqualTo(ResultType.DEBUG);
    assertThat(converter.apply(TaskType.GATLING_RECORD)).isEqualTo(ResultType.HAR);
  }

}
