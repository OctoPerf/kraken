package com.octoperf.kraken.git.entity;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class GitConfigurationTest {

  public static final GitConfiguration GIT_CONFIGURATION = GitConfiguration.builder()
      .repositoryUrl("git@github.com:geraldpereira/gatlingTest.git")
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(GIT_CONFIGURATION.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(GIT_CONFIGURATION.getClass());
  }

}