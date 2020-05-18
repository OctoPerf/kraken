package com.kraken.config.kubernetes.spring;

import com.kraken.config.kubernetes.api.KubernetesClientBuilderType;
import com.kraken.config.kubernetes.api.KubernetesProperties;
import org.junit.Test;

import static com.kraken.tests.utils.TestUtils.shouldPassEquals;
import static com.kraken.tests.utils.TestUtils.shouldPassToString;

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
    shouldPassToString(K8S_PROPERTIES);
    shouldPassEquals(K8S_PROPERTIES.getClass());
  }
}
