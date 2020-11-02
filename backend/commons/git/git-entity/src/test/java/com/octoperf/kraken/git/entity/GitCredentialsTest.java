package com.octoperf.kraken.git.entity;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class GitCredentialsTest {

  public static final GitCredentials GIT_CREDENTIALS = GitCredentials.builder()
      .privateKey("private")
      .publicKey("public")
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(GIT_CREDENTIALS.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(GIT_CREDENTIALS.getClass());
  }

  @Test
  public void shouldPassString() {
    TestUtils.shouldPassToString(GIT_CREDENTIALS.getClass());
  }
}