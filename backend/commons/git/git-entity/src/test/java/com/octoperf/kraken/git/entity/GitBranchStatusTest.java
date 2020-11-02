package com.octoperf.kraken.git.entity;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class GitBranchStatusTest {

  public static final GitBranchStatus GIT_BRANCH_STATUS = GitBranchStatus.builder()
      .oid("oid")
      .ahead(42L)
      .behind(1337L)
      .head("head")
      .upstream("upstream")
      .build();

  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(GIT_BRANCH_STATUS.getClass());
  }

  @Test
  public void shouldPassString() {
    TestUtils.shouldPassToString(GIT_BRANCH_STATUS.getClass());
  }
}