package com.kraken.runtime.event.client.spring;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class WebRuntimeEventClientFactoryTest {

  @Test
  public void shouldTestNPE() {
    TestUtils.shouldPassNPE(WebRuntimeEventClientBuilder.class);
  }

}