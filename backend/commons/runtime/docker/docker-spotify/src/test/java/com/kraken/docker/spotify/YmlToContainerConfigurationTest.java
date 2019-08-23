package com.kraken.docker.spotify;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.spotify.docker.client.messages.ContainerConfig;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class YmlToContainerConfigurationTest {

  YmlToContainerConfiguration component;

  @Before
  public void before() {
    component = new YmlToContainerConfiguration();
  }

  @Test(expected = RuntimeException.class)
  public void shouldFailParseYML() {
    component.apply("{\nimage: \"image\"}");
  }

  @Test()
  public void shouldParseYML() {
    final ContainerConfig config = component.apply("Image: image");
    Assertions.assertThat(config.image()).isEqualTo("image");
  }

  @Test()
  public void shouldParseComplexYML() throws IOException {
    final var url = Resources.getResource("config.yml");
    String yml = Resources.toString(url, Charsets.UTF_8);
    final ContainerConfig config = component.apply(yml);
    Assertions.assertThat(config.image()).isEqualTo("image");
    final var hostConfig = config.hostConfig();
    Assertions.assertThat(hostConfig).isNotNull();
    Assertions.assertThat(hostConfig.privileged()).isTrue();
  }
}