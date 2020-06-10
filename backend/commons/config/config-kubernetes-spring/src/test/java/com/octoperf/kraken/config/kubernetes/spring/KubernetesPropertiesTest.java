package com.octoperf.kraken.config.kubernetes.spring;

import com.octoperf.kraken.config.kubernetes.api.KubernetesClientBuilderType;
import com.octoperf.kraken.config.kubernetes.api.KubernetesProperties;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class KubernetesPropertiesTest {

  public static final KubernetesProperties K8S_PROPERTIES = SpringKubernetesProperties.builder()
      .namespace("kraken")
      .pretty("true")
      .builderType(KubernetesClientBuilderType.CONFIG_PATH)
      .configPath("configPath")
      .debug(true)
      .patchHosts(true)
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassToString(K8S_PROPERTIES);
    TestUtils.shouldPassEquals(K8S_PROPERTIES.getClass());
  }
}
