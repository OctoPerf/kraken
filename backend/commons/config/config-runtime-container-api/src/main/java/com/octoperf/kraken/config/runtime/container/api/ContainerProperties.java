package com.octoperf.kraken.config.runtime.container.api;

import com.octoperf.kraken.config.api.KrakenProperties;
import com.octoperf.kraken.runtime.entity.task.TaskType;

public interface ContainerProperties extends KrakenProperties {

  String getTaskId();

  TaskType getTaskType();

  String getName();

  String getHostId();

  String getApplicationId();

  String getProjectId();

  String getUserId();
}
