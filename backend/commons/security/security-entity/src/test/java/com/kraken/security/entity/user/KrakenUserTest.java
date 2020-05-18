package com.kraken.security.entity.user;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.tests.utils.TestUtils;
import lombok.With;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class KrakenUserTest {

  public static final KrakenUser KRAKEN_USER = KrakenUser.builder()
      .access(ImmutableMap.of())
      .attributes(ImmutableMap.of())
      .clientConsents(ImmutableList.of(KrakenUserConsentTest.KRAKEN_USER_CONSENT))
      .clientRoles(ImmutableMap.of())
      .createdTimestamp(0L)
      .credentials(ImmutableList.of(KrakenCredentialTest.KRAKEN_CREDENTIAL))
      .disableableCredentialTypes(ImmutableList.of("disableableCredentialType"))
      .email("email")
      .emailVerified(true)
      .enabled(true)
      .totp(false)
      .federatedIdentities(ImmutableList.of(KrakenFederatedIdentityTest.KRAKEN_FEDERATED_IDENTITY))
      .federationLink("federationLink")
      .firstName("firstName")
      .groups(ImmutableList.of("group"))
      .id("id")
      .lastName("lastName")
      .notBefore(0)
      .origin("origin")
      .realmRoles(ImmutableList.of("realmRole"))
      .requiredActions(ImmutableList.of("requiredAction"))
      .self("self")
      .serviceAccountClientId("serviceAccountClientId")
      .username("username")
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(KRAKEN_USER.getClass());
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(KRAKEN_USER);
  }

  @Test
  public void shouldWither() {
    final var attributes = ImmutableMap.of("foo", ImmutableList.of("bar"));
    Assertions.assertThat(KRAKEN_USER.withAttributes(attributes).getAttributes()).isSameAs(attributes);
  }

  @Test
  public void shouldReturnHasAttributes() {
    Assertions.assertThat(KRAKEN_USER.withAttributes(null).hasAttribute("foo")).isFalse();
    Assertions.assertThat(KRAKEN_USER.withAttributes(ImmutableMap.of()).hasAttribute("foo")).isFalse();
    Assertions.assertThat(KRAKEN_USER.withAttributes(ImmutableMap.of("foo", ImmutableList.of())).hasAttribute("foo")).isFalse();
    Assertions.assertThat(KRAKEN_USER.withAttributes(ImmutableMap.of("foo", ImmutableList.of("bar"))).hasAttribute("foo")).isTrue();
  }

  @Test
  public void shouldReturnAttributes() {
    Assertions.assertThat(KRAKEN_USER.withAttributes(ImmutableMap.of("foo", ImmutableList.of("bar"))).getAttributes("foo")).isEqualTo(ImmutableList.of("bar"));
  }

  @Test
  public void shouldReturnAttribute() {
    Assertions.assertThat(KRAKEN_USER.withAttributes(ImmutableMap.of("foo", ImmutableList.of("bar"))).getAttribute("foo")).isEqualTo("bar");
  }
}