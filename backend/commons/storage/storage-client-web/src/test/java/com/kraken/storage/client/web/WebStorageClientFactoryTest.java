package com.kraken.storage.client.web;

import com.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class WebStorageClientFactoryTest {

  @Test
  public void shouldTestNPE() {
    TestUtils.shouldPassNPE(WebStorageClientBuilder.class);
  }

}