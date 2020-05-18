package com.kraken.analysis.entity;

import org.junit.Test;

import static com.kraken.tests.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class ResultStatusTest {

  @Test
  public void shouldIsTerminal() {
    assertThat(ResultStatus.STARTING.isTerminal()).isFalse();
    assertThat(ResultStatus.RUNNING.isTerminal()).isFalse();
    assertThat(ResultStatus.STOPPING.isTerminal()).isFalse();
    assertThat(ResultStatus.COMPLETED.isTerminal()).isTrue();
    assertThat(ResultStatus.FAILED.isTerminal()).isTrue();
    assertThat(ResultStatus.CANCELED.isTerminal()).isTrue();
  }

}