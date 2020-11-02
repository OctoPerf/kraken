package com.octoperf.kraken.git.event;

import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class GitRefreshStorageEventTest {

  public static final GitRefreshStorageEvent GIT_REFRESH_STORAGE_EVENT = GitRefreshStorageEvent.builder()
      .owner(Owner.PUBLIC).build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(GIT_REFRESH_STORAGE_EVENT.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(GIT_REFRESH_STORAGE_EVENT.getClass());
  }

  @Test
  public void shouldPassString() {
    TestUtils.shouldPassToString(GIT_REFRESH_STORAGE_EVENT.getClass());
  }
}