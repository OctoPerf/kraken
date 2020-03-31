package com.kraken.security.configuration;

import com.google.common.collect.ImmutableList;
import com.kraken.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityConfigurationTest {
  @Autowired
  WebTestClient webTestClient;

  @MockBean
  ReactiveJwtDecoder jwtDecoder;

  @Test
  public void shouldReturnUser() {
    BDDMockito.given(jwtDecoder.decode("token")).willReturn(Mono.just(JwtFactory.JWT_FACTORY.create(ImmutableList.of("USER"),
        ImmutableList.of("/default-kraken"), Optional.of("/default-kraken"))));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/test/user")
            .build())
        .header("Authorization", "Bearer token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("hello username");
  }

  @Test
  public void shouldFailAdmin() {
    BDDMockito.given(jwtDecoder.decode("token")).willReturn(Mono.just(JwtFactory.JWT_FACTORY.create(ImmutableList.of("USER"),
        ImmutableList.of("/default-kraken"), Optional.of("/default-kraken"))));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/test/admin")
            .build())
        .header("Authorization", "Bearer token")
        .exchange()
        .expectStatus().is4xxClientError();
  }
}
