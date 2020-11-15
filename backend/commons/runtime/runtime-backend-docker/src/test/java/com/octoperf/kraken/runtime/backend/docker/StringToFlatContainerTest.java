package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static com.octoperf.kraken.security.entity.token.KrakenRole.USER;
import static org.assertj.core.api.Assertions.assertThat;

public class StringToFlatContainerTest {

  public final static String CONTAINER_STRING = "6ea1e79088d9;container_one_READY;2019-09-03 11:11:11 +0200 CEST;taskId;GATLING_RUN;containerOneId;hostId;2;Container One;app;projectId;userId;Some description; test!";
  private final StringToFlatContainer stringToFlatContainer = new StringToFlatContainer();

  @Test
  public void shouldConvert(){
    final var container = stringToFlatContainer.apply(CONTAINER_STRING);
    assertThat(container).isEqualTo(FlatContainer.builder()
        .id("6ea1e79088d9")
        .name("containerOneId")
        .hostId("hostId")
        .taskId("taskId")
        .label("Container One")
        .description("Some description; test!")
        .startDate(1567501871000L)
        .status(ContainerStatus.READY)
        .taskType(TaskType.GATLING_RUN)
        .expectedCount(2)
        .owner(Owner.builder().applicationId("app").projectId("projectId").userId("userId").roles(ImmutableList.of(USER)).type(OwnerType.USER).build())
        .build());
  }

  @Test
  public void shouldConvertDateFail(){
    final var currentDate = new Date().getTime();

    final var container = stringToFlatContainer.apply("fd6c4a0fb80d;container_three_STARTING;Ca va fail!!!;taskIdBis;GATLING_RUN;containerThreeId;hostId;42;name;app;projectId;userId;description");
    assertThat(container.getId()).isEqualTo("fd6c4a0fb80d");
    assertThat(container.getName()).isEqualTo("containerThreeId");
    assertThat(container.getTaskId()).isEqualTo("taskIdBis");
    assertThat(container.getHostId()).isEqualTo("hostId");
    assertThat(container.getLabel()).isEqualTo("name");
    assertThat(container.getExpectedCount()).isEqualTo(42);
    assertThat(container.getOwner()).isEqualTo(Owner.builder().applicationId("app").projectId("projectId").userId("userId").roles(ImmutableList.of(USER)).type(OwnerType.USER).build());
    assertThat(container.getStatus()).isEqualTo(ContainerStatus.STARTING);
    assertThat(container.getStartDate()).isGreaterThanOrEqualTo(currentDate);
  }
}

