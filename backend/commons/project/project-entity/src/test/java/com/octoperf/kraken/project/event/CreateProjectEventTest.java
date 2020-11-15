package com.octoperf.kraken.project.event;

import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.project.entity.Project;
import com.octoperf.kraken.project.entity.ProjectTest;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class CreateProjectEventTest {

  public static final CreateProjectEvent CREATE_PROJECT_EVENT = CreateProjectEvent.builder()
      .project(ProjectTest.PROJECT)
      .owner(Owner.PUBLIC)
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(CREATE_PROJECT_EVENT.getClass());
  }

  @Test
  public void shouldPassNPE() {
    new NullPointerTester()
        .setDefault(Project.class, ProjectTest.PROJECT)
        .setDefault(Owner.class, Owner.PUBLIC)
        .testConstructors(CREATE_PROJECT_EVENT.getClass(), PACKAGE);
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(CREATE_PROJECT_EVENT);
  }


}