package com.kraken.analysis.entity;

import com.google.common.collect.ImmutableList;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DebugEntryTest {

  public static final DebugEntry DEBUG_CHUNK = DebugEntry.builder()
      .id("id")
      .resultId("resultId")
      .date(0L)
      .requestName("requestName")
      .requestStatus("requestStatus")
      .session("session")
      .requestUrl("requestUrl")
      .requestHeaders(ImmutableList.of(HttpHeaderTest.HTTP_HEADER))
      .requestCookies(ImmutableList.of())
      .requestBodyFile("requestBodyFile")
      .responseStatus("responseStatus")
      .responseHeaders(ImmutableList.of(HttpHeaderTest.HTTP_HEADER))
      .responseBodyFile("responseBodyFile")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(DEBUG_CHUNK);
  }

  @Test
  public void shouldWither() {
    assertThat(DEBUG_CHUNK.withResultId("otherId").getResultId()).isEqualTo("otherId");
    assertThat(DEBUG_CHUNK.withRequestBodyFile("withRequestBodyFile").getRequestBodyFile()).isEqualTo("withRequestBodyFile");
    assertThat(DEBUG_CHUNK.withResponseBodyFile("withResponseBodyFile").getResponseBodyFile()).isEqualTo("withResponseBodyFile");
  }

}
