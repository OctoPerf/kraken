package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.runtime.backend.api.EnvironmentLabels;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

import static com.octoperf.kraken.security.entity.token.KrakenRole.USER;

@Component
@Slf4j
final class StringToFlatContainer implements Function<String, FlatContainer> {

  public static String FORMAT = String.format("{{.ID}};{{.Names}};{{.CreatedAt}};{{.Label \"%s\"}};{{.Label \"%s\"}};{{.Label \"%s\"}};{{.Label \"%s\"}};{{.Label \"%s\"}};{{.Label \"%s\"}};{{.Label \"%s\"}};{{.Label \"%s\"}};{{.Label \"%s\"}};{{.Label \"%s\"}}",      EnvironmentLabels.COM_OCTOPERF_TASKID,
      EnvironmentLabels.COM_OCTOPERF_TASKTYPE,
      EnvironmentLabels.COM_OCTOPERF_CONTAINER_NAME,
      EnvironmentLabels.COM_OCTOPERF_HOSTID,
      EnvironmentLabels.COM_OCTOPERF_EXPECTED_COUNT,
      EnvironmentLabels.COM_OCTOPERF_LABEL,
      EnvironmentLabels.COM_OCTOPERF_APPLICATION_ID,
      EnvironmentLabels.COM_OCTOPERF_PROJECT_ID,
      EnvironmentLabels.COM_OCTOPERF_USER_ID,
      EnvironmentLabels.COM_OCTOPERF_DESCRIPTION);
  private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z z";

  @Override
  public FlatContainer apply(final String str) {
    final var split = str.split("[;]", 13);
    final var id = split[0];
    final var status = split[1];
    final var dateStr = split[2];
    final var taskId = split[3];
    final var taskType = split[4];
    final var name = split[5];
    final var hostId = split[6];
    final var expectedCount = split[7];
    final var label = split[8];
    final var applicationId = split[9];
    final var projectId = split[10];
    final var userId = split[11];
    final var description = split[12];

    var date = new Date().getTime();
    try {
      date = new SimpleDateFormat(DATE_FORMAT).parse(dateStr).getTime();
    } catch (ParseException e) {
      log.error("Failed to parse container date", e);
    }

    return FlatContainer.builder()
        .id(id)
        .hostId(hostId)
        .taskId(taskId)
        .taskType(TaskType.valueOf(taskType))
        .label(label)
        .name(name)
        .description(description)
        .startDate(date)
        .status(ContainerStatus.parse(status))
        .expectedCount(Integer.valueOf(expectedCount))
        .owner(Owner.builder()
            .userId(userId)
            .projectId(projectId)
            .applicationId(applicationId)
            .roles(ImmutableList.of(USER))
            .type(OwnerType.USER)
            .build())
        .build();
  }
}
