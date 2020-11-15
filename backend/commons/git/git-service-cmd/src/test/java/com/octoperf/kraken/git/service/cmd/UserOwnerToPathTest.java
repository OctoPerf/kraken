package com.octoperf.kraken.git.service.cmd;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
class UserOwnerToPathTest {

  @Mock
  ApplicationProperties properties;

  UserOwnerToPath userOwnerToPath;

  @BeforeEach
  public void beforeEach() {
    userOwnerToPath = new UserOwnerToPath(properties);
  }

  @Test
  void shouldPassNPE() {
    TestUtils.shouldPassNPE(UserOwnerToPath.class);
  }

  @Test
  void shouldApplyFailOwnerType() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> userOwnerToPath.apply(Owner.builder().type(OwnerType.APPLICATION).build()));
  }

  @Test
  void shouldApplyFailEmptyUserId() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> userOwnerToPath.apply(Owner.builder().type(OwnerType.USER).build()));
  }

  @Test
  void shouldApplyFailAdmin() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> userOwnerToPath.apply(Owner.builder().type(OwnerType.USER).userId("userId").roles(ImmutableList.of(KrakenRole.ADMIN)).build()));
  }

  @Test
  void shouldApply() {
    BDDMockito.given(properties.getData()).willReturn("testDir");
    org.assertj.core.api.Assertions.assertThat(userOwnerToPath.apply(Owner.builder()
        .type(OwnerType.USER)
        .userId("userId")
        .projectId("projectId")
        .applicationId("applicationId")
        .roles(ImmutableList.of(KrakenRole.USER)).build())).isEqualTo(Paths.get("testDir", "users", "userId", "projectId", "applicationId"));
  }
}