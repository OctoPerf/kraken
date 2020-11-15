package com.octoperf.kraken.project.entity;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class ProjectTest {

  public static final Project PROJECT = Project.builder()
      .id("id")
      .name("name")
      .applicationId("applicationId")
      .createDate(42L)
      .updateDate(42L)
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(PROJECT.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(PROJECT.getClass());
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(PROJECT);
  }


}