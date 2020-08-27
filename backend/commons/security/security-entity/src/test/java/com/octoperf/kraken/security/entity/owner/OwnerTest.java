package com.octoperf.kraken.security.entity.owner;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import com.octoperf.kraken.tests.utils.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class OwnerTest {

  public static final Owner USER_OWNER = Owner.builder()
      .userId("userId")
      .projectId("projectId")
      .applicationId("applicationId")
      .roles(ImmutableList.of(KrakenRole.USER))
      .type(OwnerType.USER)
      .build();

  public static final Owner PROJECT_OWNER = Owner.builder()
      .projectId("projectId")
      .applicationId("applicationId")
      .type(OwnerType.PROJECT)
      .build();

  public static final Owner APPLICATION_OWNER = Owner.builder()
      .applicationId("applicationId")
      .type(OwnerType.APPLICATION)
      .build();

  public static final Owner PUBLIC_OWNER = Owner.PUBLIC;

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(USER_OWNER);
  }

}
