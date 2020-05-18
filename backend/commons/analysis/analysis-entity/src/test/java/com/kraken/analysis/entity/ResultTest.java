package com.kraken.analysis.entity;

import org.junit.Test;

import static com.kraken.tests.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class ResultTest {

  public static final Result RESULT = Result.builder()
      .id("id")
      .startDate(42L)
      .endDate(1337L)
      .status(ResultStatus.STARTING)
      .description("description")
      .type(ResultType.RUN)
      .build();

  public static final Result DEBUG_RESULT = Result.builder()
      .id("id")
      .startDate(42L)
      .endDate(1337L)
      .status(ResultStatus.STARTING)
      .description("description")
      .type(ResultType.DEBUG)
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
