package com.octoperf.kraken.git.entity;

import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class GitLogTest {

  public static final GitLog GIT_LOG = GitLog.builder()
      .owner(Owner.PUBLIC)
      .text("text")
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(GIT_LOG.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(GIT_LOG.getClass());
  }

  @Test
  public void shouldPassString() {
    TestUtils.shouldPassToString(GIT_LOG.getClass());
  }
}