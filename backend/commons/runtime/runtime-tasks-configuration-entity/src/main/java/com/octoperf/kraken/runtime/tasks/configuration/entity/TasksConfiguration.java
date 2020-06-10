package com.octoperf.kraken.runtime.tasks.configuration.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;


@Value
@Builder(toBuilder = true)
public class TasksConfiguration {

  @NonNull List<TaskConfiguration> tasks;

  @JsonCreator
  TasksConfiguration(
      @NonNull @JsonProperty("tasks") final List<TaskConfiguration> tasks) {
    super();
    this.tasks = tasks;
  }
}
