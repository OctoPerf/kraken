package com.kraken.runtime.tasks.configuration.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class TasksConfigurationTest {

  public static final TasksConfiguration TASKS_CONFIGURATION = TasksConfiguration.builder()
      .tasks(ImmutableList.of(TaskConfigurationTest.TASK_CONFIGURATION))
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(TASKS_CONFIGURATION.getClass());
    new NullPointerTester().setDefault(TaskConfiguration.class, TaskConfigurationTest.TASK_CONFIGURATION).testConstructors(TASKS_CONFIGURATION.getClass(), PACKAGE);
    TestUtils.shouldPassToString(TASKS_CONFIGURATION);
  }

}
