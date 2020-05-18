package com.kraken.analysis.entity;

import com.google.common.collect.ImmutableList;
import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DebugEntryTest {

  public static final DebugEntry DEBUG_ENTRY = DebugEntry.builder()
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
    TestUtils.shouldPassAll(DEBUG_ENTRY);
  }

  @Test
  public void shouldWither() {
    assertThat(DEBUG_ENTRY.withResultId("otherId").getResultId()).isEqualTo("otherId");
    assertThat(DEBUG_ENTRY.withRequestBodyFile("withRequestBodyFile").getRequestBodyFile()).isEqualTo("withRequestBodyFile");
    assertThat(DEBUG_ENTRY.withResponseBodyFile("withResponseBodyFile").getResponseBodyFile()).isEqualTo("withResponseBodyFile");
  }

}
