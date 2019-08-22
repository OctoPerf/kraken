package com.kraken.commons.command.executor;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CommandConfigurationTest {

  @Test
  public void shouldReturnSubscriptionsMap(){
    Assertions.assertThat(new CommandConfiguration().subscriptionsMap()).isNotNull();
  }
}
