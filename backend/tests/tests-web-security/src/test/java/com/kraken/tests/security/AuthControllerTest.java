package com.kraken.tests.security;

import com.google.common.collect.ImmutableList;
import com.kraken.Application;
import com.kraken.security.decoder.api.TokenDecoder;
import com.kraken.security.entity.owner.UserOwner;
import com.kraken.security.entity.token.KrakenTokenUserTest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

import static com.kraken.security.entity.token.KrakenRole.ADMIN;
import static com.kraken.security.entity.token.KrakenRole.USER;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AuthControllerTest {

  @Autowired
  protected WebTestClient webTestClient;
  @MockBean
  protected ReactiveJwtDecoder jwtDecoder;
  @MockBean
  protected TokenDecoder tokenDecoder;


  protected String applicationId = "app";

  @Before
  public void setUp() throws IOException {
    // User
    given(jwtDecoder.decode("user-token")).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create("user-token", ImmutableList.of(USER.name()),
        ImmutableList.of("/default-kraken"), Optional.of("/default-kraken"))));
    given(tokenDecoder.decode("user-token")).willReturn(KrakenTokenUserTest.KRAKEN_USER);
    // Admin
    given(jwtDecoder.decode("admin-token")).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create("admin-token", ImmutableList.of(ADMIN.name()),
        ImmutableList.of("/default-kraken"), Optional.of("/default-kraken"))));
    given(tokenDecoder.decode("admin-token")).willReturn(KrakenTokenUserTest.KRAKEN_ADMIN);
    // Admin
    given(jwtDecoder.decode("api-token")).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create("api-token", ImmutableList.of("API"),
        ImmutableList.of(), Optional.empty())));
    given(tokenDecoder.decode("api-token")).willReturn(KrakenTokenUserTest.KRAKEN_API);
    // No role
    given(jwtDecoder.decode("no-role-token")).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create("no-role-token", ImmutableList.of(),
        ImmutableList.of(), Optional.empty())));
    given(tokenDecoder.decode("no-role-token")).willReturn(KrakenTokenUserTest.KRAKEN_ADMIN);


    // WebTestClient set default header for ApplicationId
    webTestClient = webTestClient.mutate().defaultHeader("ApplicationId", applicationId).build();
  }

  protected UserOwner userOwner() {
    return UserOwner.builder()
        .applicationId(applicationId)
        .userId(KrakenTokenUserTest.KRAKEN_USER.getUserId())
        .roles(KrakenTokenUserTest.KRAKEN_USER.getRoles())
        .build();
  }

  protected UserOwner adminOwner() {
    return UserOwner.builder()
        .applicationId(applicationId)
        .userId(KrakenTokenUserTest.KRAKEN_ADMIN.getUserId())
        .roles(KrakenTokenUserTest.KRAKEN_ADMIN.getRoles())
        .build();
  }

}
