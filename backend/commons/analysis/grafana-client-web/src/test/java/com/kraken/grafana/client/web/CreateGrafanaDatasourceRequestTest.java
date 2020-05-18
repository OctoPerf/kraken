package com.kraken.grafana.client.web;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class CreateGrafanaDatasourceRequestTest {

  public static final CreateGrafanaDatasourceRequest REQUEST = CreateGrafanaDatasourceRequest.builder()
      .name("name")
      .access("access")
      .type("type")
      .isDefault(true)
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(REQUEST.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(CreateGrafanaDatasourceRequest.class);
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(REQUEST);
  }

}