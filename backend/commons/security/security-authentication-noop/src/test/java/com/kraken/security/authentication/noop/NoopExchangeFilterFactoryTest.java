package com.kraken.security.authentication.noop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.kraken.security.authentication.api.AuthenticationMode.NOOP;
import static org.assertj.core.api.Assertions.assertThat;

public class NoopExchangeFilterFactoryTest {

  NoopExchangeFilterFactory exchangeFilterFactory;

  @BeforeEach
  public void setUp() {
    exchangeFilterFactory = new NoopExchangeFilterFactory();
  }

  @Test
  public void shouldCreate() {
    assertThat(exchangeFilterFactory.create("userId")).isInstanceOf(NoopExchangeFilter.class);
  }

  @Test
  public void shouldGetMode() {
    assertThat(exchangeFilterFactory.getMode()).isEqualTo(NOOP);
  }

}