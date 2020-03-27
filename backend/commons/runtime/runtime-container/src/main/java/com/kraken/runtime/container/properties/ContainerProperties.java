package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.task.TaskType;

public interface ContainerProperties {

  String getTaskId();

  TaskType getTaskType();

  String getName();

  String getHostId();
}
