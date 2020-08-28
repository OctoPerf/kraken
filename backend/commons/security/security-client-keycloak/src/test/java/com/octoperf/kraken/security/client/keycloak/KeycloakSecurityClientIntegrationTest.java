package com.octoperf.kraken.security.client.keycloak;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.security.client.api.SecurityClient;
import com.octoperf.kraken.security.client.api.SecurityClientBuilder;
import com.octoperf.kraken.security.decoder.api.TokenDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
// Start keycloak before running
@Tag("integration")
public class KeycloakSecurityClientIntegrationTest {

  @Autowired
  SecurityClientBuilder clientBuilder;
  @Autowired
  SecurityClientProperties properties;
  @Autowired
  TokenDecoder decoder;

  SecurityClient client;

  @BeforeEach
  public void setUp() {
    client = clientBuilder.build().block();
  }

  @Test
  public void shouldImpersonate() throws IOException {
    final var impersonated = client.impersonate(properties.getApi(), "e09920ef-843b-41d3-9478-b0621b81c600").block();
    assertThat(impersonated).isNotNull();
    System.out.println(impersonated);
    System.out.println(decoder.decode(impersonated.getAccessToken()));
    final var refreshedToken = client.refreshToken(properties.getApi(), impersonated.getRefreshToken()).block();
    assertThat(refreshedToken).isNotNull();
    System.out.println(refreshedToken);
    System.out.println(decoder.decode(refreshedToken.getAccessToken()));
  }

  @Test
  public void shouldClientLogin() {
    final var loginToken = client.clientLogin(properties.getApi()).block();
    assertThat(loginToken).isNotNull();
    System.out.println(loginToken);
  }

  @Test
  public void shouldLoginExchangeRefresh() {
    final var loginToken = client.userLogin(properties.getWeb(), "contact@octoperf.com", "kraken").block();
    assertThat(loginToken).isNotNull();
    System.out.println(loginToken);
    final var containerToken = client.exchangeToken(properties.getContainer(), loginToken.getAccessToken()).block();
    assertThat(containerToken).isNotNull();
    System.out.println(containerToken);
    final var refreshedToken = client.refreshToken(properties.getContainer(), containerToken.getRefreshToken()).block();
    assertThat(refreshedToken).isNotNull();
    System.out.println(refreshedToken);
    final var refreshedToken2 = client.refreshToken(properties.getContainer(), containerToken.getRefreshToken()).block();
    assertThat(refreshedToken2).isNotNull();
    System.out.println(refreshedToken2);
  }

}