package com.kraken.runtime.docker;

import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
final class StringToContainer implements Function<String, Container> {

  public static String FORMAT = "{{.ID}};{{.Names}};{{.CreatedAt}};{{.Label \"com.kraken.taskId\"}};{{.Label \"com.kraken.taskType\"}};{{.Label \"com.kraken.containerId\"}};{{.Label \"com.kraken.groupId\"}};{{.Label \"com.kraken.description\"}}";
  private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z z";

  @Override
  public Container apply(final String str) {
    final var split = str.split("[;_]", 9);
    final var id = split[0];
    final var name = split[1];
    final var status = split[2];
    final var dateStr = split[3];
    final var taskId = split[4];
    final var taskType = split[5];
    final var containerId = split[6];
    final var groupId = split[7];
    final var description = split[8];

    var date = new Date().getTime();
    try {
      date = new SimpleDateFormat(DATE_FORMAT).parse(dateStr).getTime();
    } catch (ParseException e) {
      log.error("Failed to parse container date", e);
    }

    return Container.builder()
        .id(id)
        .containerId(containerId)
        .groupId(groupId)
        .taskId(taskId)
        .taskType(TaskType.valueOf(taskType))
        .name(name)
        .description(description)
        .startDate(date)
        .status(ContainerStatus.valueOf(status))
        .build();
  }
}
