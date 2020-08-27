package com.octoperf.kraken.tests.web.security;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.security.decoder.api.TokenDecoder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import com.octoperf.kraken.security.entity.token.KrakenTokenUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AuthControllerTest {

  @Autowired
  protected WebTestClient webTestClient;
  @MockBean
  protected ReactiveJwtDecoder jwtDecoder;
  @MockBean
  protected TokenDecoder tokenDecoder;

  protected String applicationId = "app";
  protected String projectId = "projectid9"; // 10 char long

  @BeforeEach
  public void setUp() throws IOException {
    // User
    BDDMockito.given(jwtDecoder.decode("user-token")).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create("user-token", ImmutableList.of(KrakenRole.USER.name()),
        ImmutableList.of("/default-kraken"), Optional.of("/default-kraken"))));
    BDDMockito.given(tokenDecoder.decode("user-token")).willReturn(KrakenTokenUserTest.KRAKEN_USER);
    // Admin
    BDDMockito.given(jwtDecoder.decode("admin-token")).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create("admin-token", ImmutableList.of(KrakenRole.ADMIN.name()),
        ImmutableList.of("/default-kraken"), Optional.of("/default-kraken"))));
    BDDMockito.given(tokenDecoder.decode("admin-token")).willReturn(KrakenTokenUserTest.KRAKEN_ADMIN);
    // Admin
    BDDMockito.given(jwtDecoder.decode("api-token")).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create("api-token", ImmutableList.of("API"),
        ImmutableList.of(), Optional.empty())));
    BDDMockito.given(tokenDecoder.decode("api-token")).willReturn(KrakenTokenUserTest.KRAKEN_API);
    // No role
    BDDMockito.given(jwtDecoder.decode("no-role-token")).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create("no-role-token", ImmutableList.of(),
        ImmutableList.of(), Optional.empty())));
    BDDMockito.given(tokenDecoder.decode("no-role-token")).willReturn(KrakenTokenUserTest.KRAKEN_ADMIN);


    // WebTestClient set default header for ApplicationId
    webTestClient = webTestClient.mutate()
        .defaultHeader("ApplicationId", applicationId)
        .defaultHeader("ProjectId", projectId)
        .build();
  }

  protected Owner userOwner() {
    return Owner.builder()
        .applicationId(applicationId)
        .projectId(projectId)
        .userId(KrakenTokenUserTest.KRAKEN_USER.getUserId())
        .roles(KrakenTokenUserTest.KRAKEN_USER.getRoles())
        .type(OwnerType.USER)
        .build();
  }

  protected Owner adminOwner() {
    return Owner.builder()
        .applicationId(applicationId)
        .projectId(projectId)
        .userId(KrakenTokenUserTest.KRAKEN_ADMIN.getUserId())
        .roles(KrakenTokenUserTest.KRAKEN_ADMIN.getRoles())
        .type(OwnerType.USER)
        .build();
  }

}
