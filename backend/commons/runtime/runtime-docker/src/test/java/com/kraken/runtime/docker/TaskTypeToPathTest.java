package com.kraken.runtime.docker;

import com.kraken.runtime.entity.TaskType;
import com.kraken.tools.configuration.properties.ApplicationPropertiesTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTypeToPathTest {


  private final TaskTypeToPath toPath = new TaskTypeToPath(ApplicationPropertiesTest.APPLICATION_PROPERTIES);

  @Test
  public void shouldConvert(){
    final var path = toPath.apply(TaskType.RUN);
    assertThat(path).isEqualTo("testDir/run");
  }
}

