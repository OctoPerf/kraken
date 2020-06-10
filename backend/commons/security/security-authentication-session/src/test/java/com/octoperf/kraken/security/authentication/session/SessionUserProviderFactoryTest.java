package com.octoperf.kraken.security.authentication.session;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.SESSION;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class SessionUserProviderFactoryTest {

  @Mock
  SessionUserProvider provider;

  SessionUserProviderFactory factory;

  @BeforeEach
  public void setUp() {
    factory = new SessionUserProviderFactory(provider);
  }

  @Test
  public void shouldCreate() {
    final var userProvider = factory.create("userId");
    assertThat(userProvider).isSameAs(provider);
  }

  @Test
  public void shouldGetMode() {
    assertThat(factory.getMode()).isEqualTo(SESSION);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(SessionUserProviderFactory.class);
  }

}