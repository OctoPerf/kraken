package com.octoperf.kraken.git.event;

import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

class GitStatusUpdateEventTest {

  public static final GitStatusUpdateEvent GIT_STATUS_UPDATE_EVENT = GitStatusUpdateEvent.builder()
      .owner(Owner.PUBLIC).build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(GIT_STATUS_UPDATE_EVENT.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(GIT_STATUS_UPDATE_EVENT.getClass());
  }

  @Test
  public void shouldPassString() {
    TestUtils.shouldPassToString(GIT_STATUS_UPDATE_EVENT.getClass());
  }
}