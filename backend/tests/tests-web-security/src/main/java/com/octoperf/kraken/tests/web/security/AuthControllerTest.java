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

  private static final String USER_TOKEN = "user-token";
  private static final String DEFAULT_KRAKEN = "/default-kraken";
  private static final String API_TOKEN = "api-token";
  private static final String NO_ROLE_TOKEN = "no-role-token";
  private static final String ADMIN_TOKEN = "admin-token";
  private static final String APPLICATION_ID = "ApplicationId";
  private static final String PROJECT_ID = "ProjectId";
  
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
    BDDMockito.given(jwtDecoder.decode(USER_TOKEN)).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create(USER_TOKEN, ImmutableList.of(KrakenRole.USER.name()),
        ImmutableList.of(DEFAULT_KRAKEN), Optional.of(DEFAULT_KRAKEN))));
    BDDMockito.given(tokenDecoder.decode(USER_TOKEN)).willReturn(KrakenTokenUserTest.KRAKEN_USER);
    // Admin
    BDDMockito.given(jwtDecoder.decode(ADMIN_TOKEN)).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create(ADMIN_TOKEN, ImmutableList.of(KrakenRole.ADMIN.name()),
        ImmutableList.of(DEFAULT_KRAKEN), Optional.of(DEFAULT_KRAKEN))));
    BDDMockito.given(tokenDecoder.decode(ADMIN_TOKEN)).willReturn(KrakenTokenUserTest.KRAKEN_ADMIN);
    // Admin
    BDDMockito.given(jwtDecoder.decode(API_TOKEN)).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create(API_TOKEN, ImmutableList.of("API"),
        ImmutableList.of(), Optional.empty())));
    BDDMockito.given(tokenDecoder.decode(API_TOKEN)).willReturn(KrakenTokenUserTest.KRAKEN_API);
    // No role
    BDDMockito.given(jwtDecoder.decode(NO_ROLE_TOKEN)).willReturn(Mono.just(JwtTestFactory.JWT_FACTORY.create(NO_ROLE_TOKEN, ImmutableList.of(),
        ImmutableList.of(), Optional.empty())));
    BDDMockito.given(tokenDecoder.decode(NO_ROLE_TOKEN)).willReturn(KrakenTokenUserTest.KRAKEN_ADMIN);


    // WebTestClient set default header for ApplicationId
    webTestClient = webTestClient.mutate()
        .defaultHeader(APPLICATION_ID, applicationId)
        .defaultHeader(PROJECT_ID, projectId)
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
