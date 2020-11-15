package com.octoperf.kraken.grafana.client.web;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class GrafanaFolderResponseTest {

  public static final GrafanaFolderResponse RESPONSE = GrafanaFolderResponse.builder()
      .id(2L)
      .uid("uid")
      .title("title")
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(RESPONSE.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(FindGrafanaUserResponse.class);
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(RESPONSE);
  }

}