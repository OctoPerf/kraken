package com.kraken.security.authentication.session;

import com.kraken.tests.security.AuthControllerTest;
import org.junit.Test;

public class SecurityConfigurationTest extends AuthControllerTest {

  @Test
  public void shouldReturnUser() {
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/test/user")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("hello username");
  }

  @Test
  public void shouldFailAdmin() {
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/test/admin")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().is4xxClientError();
  }
}
