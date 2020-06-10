package com.octoperf.kraken.tools.configuration.reactive;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReactiveConfigurationTest {

  @Test
  void shouldCreateBean(){
    Assertions.assertThat(new ReactiveConfiguration().reactiveWebServerFactory()).isNotNull();
  }
}