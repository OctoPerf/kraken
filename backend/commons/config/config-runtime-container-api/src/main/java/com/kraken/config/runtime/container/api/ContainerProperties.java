package com.kraken.config.runtime.container.api;

import com.kraken.config.api.KrakenProperties;
import com.kraken.runtime.entity.task.TaskType;

public interface ContainerProperties extends KrakenProperties {

  String getTaskId();

  TaskType getTaskType();

  String getName();

  String getHostId();

  String getApplicationId();

  String getUserId();
}
