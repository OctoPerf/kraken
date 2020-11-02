package com.octoperf.kraken.git.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.git.event.GitRefreshStorageEventTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class GitStatusTest {

  public static final GitStatus GIT_STATUS = GitStatus.builder()
      .branch(GitBranchStatus.builder().build())
      .changed(ImmutableList.of(GitFileStatusTest.GIT_FILE_STATUS))
      .unmerged(ImmutableList.of(GitFileStatusTest.GIT_FILE_STATUS))
      .ignored(ImmutableList.of("ignored"))
      .untracked(ImmutableList.of("untracked"))
      .renamedCopied(ImmutableList.of(GitRenamedCopiedStatusTest.GIT_RENAMED_COPIED_STATUS))
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(GIT_STATUS.getClass());
  }

  @Test
  public void shouldPassNPE() {
    new NullPointerTester()
        .setDefault(GitBranchStatus.class, GitBranchStatusTest.GIT_BRANCH_STATUS)
        .testConstructors(GIT_STATUS.getClass(), NullPointerTester.Visibility.PACKAGE);
  }

  @Test
  public void shouldPassString() {
    TestUtils.shouldPassToString(GIT_STATUS.getClass());
  }
}