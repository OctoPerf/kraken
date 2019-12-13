package com.kraken.runtime.server.event;

import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.event.TaskTypeToResultType;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTypeToResultTypeTest {

  private final TaskTypeToResultType converter = new TaskTypeToResultType();

  @Test
  public void shouldConvert() {
    assertThat(converter.apply(TaskType.RUN)).isEqualTo(ResultType.RUN);
    assertThat(converter.apply(TaskType.DEBUG)).isEqualTo(ResultType.DEBUG);
    assertThat(converter.apply(TaskType.RECORD)).isEqualTo(ResultType.HAR);
  }

  @Test
  public void shouldConvertAll() {
    Arrays.asList(TaskType.values()).forEach(type -> {
      assertThat(converter.apply(type)).isNotNull();
    });
  }
}
