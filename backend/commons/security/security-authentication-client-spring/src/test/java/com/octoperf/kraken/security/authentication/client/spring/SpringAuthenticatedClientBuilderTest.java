package com.octoperf.kraken.security.authentication.client.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.security.authentication.api.UserProviderFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerTest;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SpringAuthenticatedClientBuilderTest {

  private static class TestAuthenticatedClient implements AuthenticatedClient {
    public final Owner owner;

    public TestAuthenticatedClient(final Owner owner) {
      this.owner = owner;
    }
  }

  private static class TestAuthenticatedClientBuilder extends SpringAuthenticatedClientBuilder<TestAuthenticatedClient> {

    public TestAuthenticatedClientBuilder(final List<UserProviderFactory> userProviderFactories) {
      super(userProviderFactories);
    }

    @Override
    public Mono<TestAuthenticatedClient> build(final AuthenticatedClientBuildOrder order) {
      return getOwner(order).map(owner -> new TestAuthenticatedClient(owner));
    }
  }

  @Mock
  UserProviderFactory providerFactory;
  @Mock
  UserProvider provider;

  TestAuthenticatedClientBuilder factory;

  @BeforeEach
  public void setUp() {
    factory = new TestAuthenticatedClientBuilder(ImmutableList.of(providerFactory));
  }

  @Test
  public void shouldCreateDefault() {
    final var client = factory.build(AuthenticatedClientBuildOrder.builder().build()).block();
    assertNotNull(client);
    assertEquals(client.owner, Owner.PUBLIC);
  }

  @Test
  public void shouldCreateNoop() {
    final var client = factory.build(AuthenticatedClientBuildOrder.builder().mode(NOOP).build()).block();
    assertNotNull(client);
    assertEquals(client.owner, Owner.PUBLIC);
  }

  @Test
  public void shouldCreateServiceAccount() {
    final var client = factory.build(AuthenticatedClientBuildOrder.builder()
        .mode(SERVICE_ACCOUNT)
        .userId("userId")
        .projectId("projectId")
        .applicationId("applicationId")
        .build()).block();
    assertNotNull(client);
    assertEquals(client.owner, Owner.builder()
        .userId("userId")
        .applicationId("applicationId")
        .projectId("projectId")
        .roles(ImmutableList.of(KrakenRole.ADMIN, KrakenRole.API))
        .type(OwnerType.USER)
        .build());
  }

  @Test
  public void shouldCreateSession() {
    given(providerFactory.getMode()).willReturn(SESSION);
    given(providerFactory.create(anyString())).willReturn(provider);
    given(provider.getOwner(anyString(), anyString())).willReturn(Mono.just(OwnerTest.USER_OWNER));
    final var client = factory.build(AuthenticatedClientBuildOrder.builder()
        .mode(SESSION)
        .userId("userId")
        .projectId("projectId")
        .applicationId("applicationId")
        .build()).block();
    assertNotNull(client);
    assertEquals(client.owner, OwnerTest.USER_OWNER);
  }

  @Test
  public void shouldCreateImpersonate() {
    final var client = factory.build(AuthenticatedClientBuildOrder.builder()
        .mode(IMPERSONATE)
        .userId("userId")
        .projectId("projectId")
        .applicationId("applicationId")
        .build()).block();
    assertNotNull(client);
    assertEquals(client.owner, Owner.builder()
        .userId("userId")
        .applicationId("applicationId")
        .projectId("projectId")
        .roles(ImmutableList.of(KrakenRole.USER))
        .type(OwnerType.USER)
        .build());
  }
}