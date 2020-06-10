package com.octoperf.kraken.security.authentication.noop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.NOOP;
import static org.assertj.core.api.Assertions.assertThat;

public class NoopUserProviderFactoryTest {

  NoopUserProviderFactory factory;

  @BeforeEach
  public void setUp() {
    factory = new NoopUserProviderFactory();
  }

  @Test
  public void shouldCreate() {
    final var userProvider = factory.create("userId");
    assertThat(userProvider).isInstanceOf(NoopUserProvider.class);
  }

  @Test
  public void shouldGetMode() {
    assertThat(factory.getMode()).isEqualTo(NOOP);
  }

}