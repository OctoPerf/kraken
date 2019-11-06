package com.kraken.runtime.docker;

import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.TaskType;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class StringToContainerTest {

  private final StringToContainer stringToContainer = new StringToContainer();

  @Test
  public void shouldConvert(){
    final var container = stringToContainer.apply("6ea1e79088d9;container_one_READY;2019-09-03 11:11:11 +0200 CEST;taskId;RUN;containerOneId;hostId;Container One;Some description; test!");
    assertThat(container).isEqualTo(Container.builder()
        .id("6ea1e79088d9")
        .containerId("containerOneId")
        .hostId("hostId")
        .taskId("taskId")
        .name("Container One")
        .description("Some description; test!")
        .startDate(1567501871000L)
        .status(ContainerStatus.READY)
        .taskType(TaskType.RUN)
        .build());
  }

  @Test
  public void shouldConvertDateFail(){
    final var currentDate = new Date().getTime();

    final var container = stringToContainer.apply("fd6c4a0fb80d;container_three_STARTING;Ca va fail!!!;taskIdBis;RUN;containerThreeId;hostId;name;description");
    assertThat(container.getId()).isEqualTo("fd6c4a0fb80d");
    assertThat(container.getContainerId()).isEqualTo("containerThreeId");
    assertThat(container.getTaskId()).isEqualTo("taskIdBis");
    assertThat(container.getHostId()).isEqualTo("hostId");
    assertThat(container.getName()).isEqualTo("name");
    assertThat(container.getStatus()).isEqualTo(ContainerStatus.STARTING);
    assertThat(container.getStartDate()).isGreaterThanOrEqualTo(currentDate);
  }
}

