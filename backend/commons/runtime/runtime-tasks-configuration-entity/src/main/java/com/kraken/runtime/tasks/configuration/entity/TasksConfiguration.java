package com.kraken.runtime.tasks.configuration.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class TasksConfiguration {

  @NonNull List<TaskConfiguration> tasks;

  @JsonCreator
  TasksConfiguration(
      @JsonProperty("tasks") final List<TaskConfiguration> tasks) {
    super();
    this.tasks = requireNonNull(tasks);
  }
}
