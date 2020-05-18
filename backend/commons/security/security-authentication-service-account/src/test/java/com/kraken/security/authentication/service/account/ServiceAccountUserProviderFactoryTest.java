package com.kraken.security.authentication.service.account;

import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.client.api.SecurityClient;
import com.kraken.security.decoder.api.TokenDecoder;
import com.kraken.tests.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.kraken.security.authentication.api.AuthenticationMode.SERVICE_ACCOUNT;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ServiceAccountUserProviderFactoryTest {

  @Mock
  SecurityClientProperties clientProperties;
  @Mock
  TokenDecoder decoder;
  @Mock
  SecurityClient client;

  ServiceAccountUserProviderFactory factory;

  @Before
  public void setUp() {
    factory = new ServiceAccountUserProviderFactory(clientProperties, decoder, client);
  }

  @Test
  public void shouldCreate() {
    final var userProvider = factory.create("userId");
    assertThat(userProvider).isNotNull();
  }

  @Test
  public void shouldGetMode() {
    assertThat(factory.getMode()).isEqualTo(SERVICE_ACCOUNT);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(ServiceAccountUserProviderFactory.class);
  }

}