package com.kraken.security.client.keycloak;

import com.kraken.Application;
import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.security.client.api.SecurityClient;
import com.kraken.security.decoder.api.TokenDecoder;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
//@Ignore("Start keycloak before running")
public class KeycloakSecurityClientIntegrationTest {

  @Autowired
  SecurityClient client;
  @Autowired
  SecurityClientProperties properties;
  @Autowired
  TokenDecoder decoder;

  @Test
  public void shouldImpersonateFail() throws IOException {
    client.impersonate(properties.getApi(), "nope").block();
  }

  @Test
  public void shouldImpersonate() throws IOException {
    final var impersonated = client.impersonate(properties.getApi(), "2e44ffae-111c-4f59-ae2b-65000de6f7b7").block();
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