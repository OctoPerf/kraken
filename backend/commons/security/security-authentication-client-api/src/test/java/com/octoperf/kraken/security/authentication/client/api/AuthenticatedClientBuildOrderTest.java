package com.octoperf.kraken.security.authentication.client.api;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.*;
import static com.octoperf.kraken.security.entity.owner.OwnerTest.USER_OWNER;
import static org.assertj.core.api.Assertions.assertThat;

class AuthenticatedClientBuildOrderTest {

  public static final AuthenticatedClientBuildOrder ORDER = AuthenticatedClientBuildOrder.builder()
      .mode(SESSION)
      .userId("userId")
      .projectId("projectId")
      .applicationId("applicationId")
      .build();


  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(ORDER);
  }

  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(ORDER.getClass());
  }

  @Test
  public void shouldBuildSession() {
    assertThat(AuthenticatedClientBuildOrder.builder().session(USER_OWNER).build()).isEqualTo(
        AuthenticatedClientBuildOrder.builder()
            .mode(SESSION)
            .applicationId(USER_OWNER.getApplicationId())
            .projectId(USER_OWNER.getProjectId())
            .build());
  }

  @Test
  public void shouldBuildImpersonate() {
    assertThat(AuthenticatedClientBuildOrder.builder().impersonate(USER_OWNER).build()).isEqualTo(
        AuthenticatedClientBuildOrder.builder()
            .mode(IMPERSONATE)
            .applicationId(USER_OWNER.getApplicationId())
            .projectId(USER_OWNER.getProjectId())
            .userId(USER_OWNER.getUserId())
            .build());
  }

  @Test
  public void shouldBuildServiceAccount() {
    assertThat(AuthenticatedClientBuildOrder.builder().serviceAccount(USER_OWNER).build()).isEqualTo(
        AuthenticatedClientBuildOrder.builder()
            .mode(SERVICE_ACCOUNT)
            .applicationId(USER_OWNER.getApplicationId())
            .projectId(USER_OWNER.getProjectId())
            .userId(USER_OWNER.getUserId())
            .build());
  }

  @Test
  public void shouldBuildContainer() {
    assertThat(AuthenticatedClientBuildOrder.builder().container("applicationId", "projectId").build()).isEqualTo(
        AuthenticatedClientBuildOrder.builder()
            .mode(CONTAINER)
            .applicationId("applicationId")
            .projectId("projectId")
            .build());
  }
}