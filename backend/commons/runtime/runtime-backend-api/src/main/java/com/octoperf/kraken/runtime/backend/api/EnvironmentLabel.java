package com.octoperf.kraken.runtime.backend.api;

import lombok.NonNull;

public enum EnvironmentLabel {
  COM_OCTOPERF_TASKID("com.octoperf/taskId"),
  COM_OCTOPERF_HOSTID("com.octoperf/hostId"),
  COM_OCTOPERF_TASKTYPE("com.octoperf/taskType"),
  COM_OCTOPERF_ACCESS("com.octoperf/access"),
  COM_OCTOPERF_APPLICATION_ID("com.octoperf/applicationId"),
  COM_OCTOPERF_USER_ID("com.octoperf/userId"),
  COM_OCTOPERF_PROJECT_ID("com.octoperf/projectId"),
  COM_OCTOPERF_DESCRIPTION("com.octoperf/description"),
  COM_OCTOPERF_EXPECTED_COUNT("com.octoperf/expectedCount"),
  COM_OCTOPERF_STATUS("com.octoperf/status.%s"),
  COM_OCTOPERF_CONTAINER_NAME("com.octoperf/containerName"),
  COM_OCTOPERF_LABEL("com.octoperf/label");

  private final String label;

  EnvironmentLabel(@NonNull final String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return this.label;
  }
}
