package com.kraken.runtime.server.event;

import com.kraken.analysis.entity.ResultType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTypeToResultTypeTest {

  private final TaskTypeToResultType converter = new TaskTypeToResultType();

  @Test
  public void shouldConvert() {
    assertThat(converter.apply("RUN")).isEqualTo(ResultType.RUN);
    assertThat(converter.apply("DEBUG")).isEqualTo(ResultType.DEBUG);
    assertThat(converter.apply("RECORD")).isEqualTo(ResultType.HAR);
  }

}
