package com.kraken.analysis.entity;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class ResultTest {

  public static final Result RESULT = Result.builder()
      .id("id")
      .startDate(0L)
      .endDate(0L)
      .status(ResultStatus.STARTING)
      .description("runDescription")
      .type(ResultType.RUN)
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(RESULT);
  }

  @Test
  public void shouldWither() {
    assertThat(RESULT.withStatus(ResultStatus.CANCELED).getStatus()).isEqualTo(ResultStatus.CANCELED);
    assertThat(RESULT.withEndDate(42L).getEndDate()).isEqualTo(42L);
  }

}
