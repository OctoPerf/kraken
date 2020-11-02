package com.octoperf.kraken.git.entity;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class GitFileStatusTest {

  public static final GitFileStatus GIT_FILE_STATUS = GitFileStatus.builder()
      .path("path")
      .xy("xy")
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(GIT_FILE_STATUS.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(GIT_FILE_STATUS.getClass());
  }

  @Test
  public void shouldPassString() {
    TestUtils.shouldPassToString(GIT_FILE_STATUS.getClass());
  }
}