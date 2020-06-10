package com.octoperf.kraken.security.authentication.client.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.security.authentication.api.UserProviderFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.PublicOwner;
import com.octoperf.kraken.security.entity.owner.UserOwner;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

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
    public Mono<TestAuthenticatedClient> build() {
      return getOwner().map(owner -> new TestAuthenticatedClient(owner));
    }
  }

  @Mock
  UserProviderFactory providerFactory;
  @Mock
  UserProvider provider;
  @Mock
  Owner owner;

  TestAuthenticatedClientBuilder factory;

  @BeforeEach
  public void setUp() {
    factory = new TestAuthenticatedClientBuilder(ImmutableList.of(providerFactory));
  }

  @Test
  public void shouldCreateDefault() {
    final var client = factory.build().block();
    assertNotNull(client);
    assertEquals(client.owner, PublicOwner.INSTANCE);
  }

  @Test
  public void shouldCreateNoop() {
    final var client = factory.mode(NOOP).build().block();
    assertNotNull(client);
    assertEquals(client.owner, PublicOwner.INSTANCE);
  }

  @Test
  public void shouldCreateServiceAccount() {
    final var client = factory.mode(SERVICE_ACCOUNT).build().block();
    assertNotNull(client);
    assertEquals(client.owner, PublicOwner.INSTANCE);
  }

  @Test
  public void shouldCreateSession() {
    given(providerFactory.getMode()).willReturn(SESSION);
    given(providerFactory.create(anyString())).willReturn(provider);
    given(provider.getOwner(anyString())).willReturn(Mono.just(owner));
    final var client = factory.mode(SESSION, "userId").applicationId("applicationId").build().block();
    assertNotNull(client);
    assertEquals(client.owner, owner);
  }

  @Test
  public void shouldCreateImpersonate() {
    final var client = factory.mode(IMPERSONATE, "userId").applicationId("applicationId").build().block();
    assertNotNull(client);
    assertEquals(client.owner, UserOwner.builder().userId("userId")
        .applicationId("applicationId")
        .roles(ImmutableList.of(KrakenRole.USER)).build());
  }

  @Test
  public void shouldCreateImpersonateFail() {
    assertThrows(IllegalArgumentException.class, () -> {
      factory.mode(CONTAINER);
    });
  }
}