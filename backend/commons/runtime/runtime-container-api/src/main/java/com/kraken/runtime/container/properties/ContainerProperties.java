package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.config.api.KrakenProperties;

public interface ContainerProperties extends KrakenProperties {

  String getTaskId();

  TaskType getTaskType();

  String getName();

  String getHostId();
}
