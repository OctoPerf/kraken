package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.task.TaskType;

public interface RuntimeContainerProperties {

  String getTaskId();

  TaskType getTaskType();

  String getContainerName();

  String getHostId();
}
