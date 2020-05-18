package com.kraken.grafana.client.web;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class UpdateGrafanaOrganizationRequestTest {

  public static final UpdateGrafanaOrganizationRequest REQUEST = UpdateGrafanaOrganizationRequest.builder()
      .name("name")
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(REQUEST.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(UpdateGrafanaOrganizationRequest.class);
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(REQUEST);
  }

}