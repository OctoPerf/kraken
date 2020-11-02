package com.octoperf.kraken.git.entity;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class GitRenamedCopiedStatusTest {

  public static final GitRenamedCopiedStatus GIT_RENAMED_COPIED_STATUS = GitRenamedCopiedStatus.builder()
      .path("path")
      .xy("xy")
      .origPath("origPath")
      .score("R100")
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(GIT_RENAMED_COPIED_STATUS.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(GIT_RENAMED_COPIED_STATUS.getClass());
  }

  @Test
  public void shouldPassString() {
    TestUtils.shouldPassToString(GIT_RENAMED_COPIED_STATUS.getClass());
  }
}