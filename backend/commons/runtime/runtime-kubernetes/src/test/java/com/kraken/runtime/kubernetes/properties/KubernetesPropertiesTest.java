package com.kraken.runtime.kubernetes.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class KubernetesPropertiesTest {

  public static final KubernetesProperties K8S_PROPERTIES = KubernetesProperties.builder()
      .namespace("kraken")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(K8S_PROPERTIES);
  }
}
