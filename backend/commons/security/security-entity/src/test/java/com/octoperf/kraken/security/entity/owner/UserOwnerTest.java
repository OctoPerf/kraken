package com.octoperf.kraken.security.entity.owner;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import com.octoperf.kraken.tests.utils.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class UserOwnerTest {

  public static final UserOwner USER_OWNER = UserOwner.builder()
      .userId("userId")
      .applicationId("applicationId")
      .roles(ImmutableList.of(KrakenRole.USER))
      .build();

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(USER_OWNER.getClass());
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(USER_OWNER);
  }

}
